package ru.mechtatell.Services;

import groovy.lang.GroovyClassLoader;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.stereotype.Service;
import ru.mechtatell.DAO.ListDAOImpl;
import ru.mechtatell.DAO.Repos.ClientRepository;
import ru.mechtatell.DAO.Repos.ComparatorRepository;
import ru.mechtatell.DAO.Repos.UserRepository;
import ru.mechtatell.Models.User;
import ru.mechtatell.Util.Collections.AdvancedList;
import ru.mechtatell.Util.Collections.MyCollection;
import ru.mechtatell.Models.ClientComparator;
import ru.mechtatell.Models.Client;
import ru.mechtatell.Models.ClientList;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.*;
import java.util.stream.Stream;

@Service
public class ClientListService {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ListDAOImpl listDAO;
    private final ComparatorRepository comparatorRepository;

    @Autowired
    public ClientListService(UserRepository userRepository, ClientRepository clientRepository, ListDAOImpl listDAO, ComparatorRepository comparatorRepository) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.listDAO = listDAO;
        this.comparatorRepository = comparatorRepository;
    }

    public long createList(ClientList list, Principal user) {
        User creator = userRepository.findByLogin(user.getName());
        list.setUser(creator);
        ClientList savedList = listDAO.save(list);
        return savedList.getId();
    }

    public List<ClientList> findAllLists(Principal user) {
        User creator = userRepository.findByLogin(user.getName());
        return listDAO.findAllByUser(creator);
    }

    public ClientList findListById(long id, Principal user) throws NotFoundException {
        User creator = userRepository.findByLogin(user.getName());

        if (listDAO.findById(id, creator).isEmpty()) {
            throw new NotFoundException("Cant find list with id " + id);
        }

        return listDAO.findById(id, creator).get();
    }

    public int getListSize(long id, Principal user) throws NotFoundException {
        ClientList clientList = findListById(id, user);
        return clientList.getClients().size();
    }

    @Transactional
    public long addClientToList(Client client, long listId, Principal user) throws NotFoundException {
        ClientList clientList = findListById(listId, user);
        client.setClientList(clientList);

        Client savedClient = clientRepository.save(client);
        return savedClient.getId();
    }

    public Client findClientById(long ClientId, long listId, Principal user) throws NotFoundException {
        if (clientRepository.findById(ClientId).isEmpty()) {
            throw new NotFoundException("Cant find Client with id " + ClientId);
        }

        ClientList clientList = findListById(listId, user);
        Client client = clientRepository.findById(ClientId).get();

        if (!clientList.getClients().contains(client)) {
            throw new NotFoundException("Client with id " + ClientId + " is in another list");
        }

        return client;
    }

    public void deleteClient(long ClientId, long listId, Principal user) throws NotFoundException {
        Client client = findClientById(ClientId, listId, user);
        client.getClientList().getClients().remove(client);
        clientRepository.delete(client);
    }

    public long addClientsToList(List<Client> clients, long listId, Principal user) {
        ClientList clientList;
        try {
            clientList = findListById(listId, user);
        } catch (NotFoundException e) {
            clientList = new ClientList();
            clientList.setName("New List");
            clientList.setUser(userRepository.findByLogin(user.getName()));
            listDAO.save(clientList);
        }

        for (Client client : clients) {
            client.setClientList(clientList);
            clientRepository.save(client);
        }

        return clients.size();
    }

    public int findOccurrencesCount(String clientString, long listId, Principal user) throws NotFoundException {
        ClientList clientList = findListById(listId, user);
        Client client = mapClient(clientString);
        return filterAndCount(clientList, client);
    }

    public ClientList shuffle(long id, Principal user) throws NotFoundException {
        ClientList clientList = findListById(id, user);
        AdvancedList<Client> shuffledCollection = toAdvancedList(clientList).shuffle();
        List<Client> shuffledList = toClientList(shuffledCollection);
        clientList.setClients(shuffledList);

        return clientList;
    }

    public ClientList sort(long id, String comparatorName, Principal user) throws Exception {
        ClientList clientList = findListById(id, user);
        Comparator<Client> comparator = findComparator(comparatorName);
        AdvancedList<Client> sortedCollection = toAdvancedList(clientList).sort(comparator);
        List<Client> sortedList = toClientList(sortedCollection);
        clientList.setClients(sortedList);

        return clientList;
    }

    public long createComparator(ClientComparator clientComparator) {
        ClientComparator savedClientComparator = comparatorRepository.save(clientComparator);
        return savedClientComparator.getId();
    }

    private Comparator<Client> loadComparator(String code) throws Exception {
        GroovyClassLoader loader = new GroovyClassLoader();

        Object o = loader.parseClass(code).getDeclaredConstructor().newInstance();
        if (!(o instanceof Comparator)) {
            throw new InstantiationException("Cant read comporator");
        }

        return (Comparator<Client>) o;
    }

    private AdvancedList<Client> toAdvancedList(ClientList clientList) throws NotFoundException {
        MyCollection<Client> myCollection = new MyCollection<>();
        clientList.getClients().forEach(myCollection::add);
        return myCollection;
    }

    private List<Client> toClientList(AdvancedList<Client> list) {
        List<Client> shuffledList = new ArrayList<>();
        for (int i = 0; i < list.size() && list.get(i).isPresent(); i++) {
            shuffledList.add(list.get(i).get());
        }
        return shuffledList;
    }

    private Comparator<Client> findComparator(String name) throws Exception {
        Comparator<Client> comparator;
        ClientComparator clientComparator = comparatorRepository.findByName(name);
        if (Objects.isNull(clientComparator)) {
            comparator = (o1, o2) -> (int) (o1.getSum() - o2.getSum());
        } else {
            comparator = loadComparator(clientComparator.getCode());
        }

        return comparator;
    }

    private Client mapClient(String clientString) {
        Map<String, Object> map = new BasicJsonParser().parseMap("{" + clientString + "}");

        Client client = new Client();
        if (map.containsKey("name")) {
            client.setName((String) map.get("name"));
        }

        if (map.containsKey("age")) {
            client.setAge(Integer.parseInt(map.get("age").toString()));
        }

        if (map.containsKey("sum")) {
            client.setSum(Double.parseDouble(map.get("sum").toString()));
        }

        return client;
    }

    private int filterAndCount(ClientList clientList, Client client) {
        Stream<Client> filteredList = clientList.getClients().stream();
        boolean check = false;

        if (client.getName() != null && !client.getName().isEmpty()) {
            filteredList = filteredList
                    .filter(e -> e.getName().equals(client.getName()));
            check = true;
        }

        if (client.getAge() != 0) {
            filteredList = filteredList
                    .filter(e -> e.getAge() == (client.getAge()));
            check = true;
        }

        if (client.getSum() != 0) {
            filteredList = filteredList
                    .filter(e -> e.getSum() == (client.getSum()));
            check = true;
        }

        return check ? (int) filteredList.count() : 0;
    }
}
