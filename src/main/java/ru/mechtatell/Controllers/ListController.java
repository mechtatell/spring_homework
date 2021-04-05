package ru.mechtatell.Controllers;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mechtatell.Models.DTO.ListInfoDTO;
import ru.mechtatell.Models.DTO.ClientDTO;
import ru.mechtatell.Models.DTO.ClientListDTO;
import ru.mechtatell.Models.ClientComparator;
import ru.mechtatell.Models.Client;
import ru.mechtatell.Services.ClientListService;

import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lists")
public class ListController {

    private final ClientListService service;

    @Autowired
    public ListController(ClientListService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ListInfoDTO>> getLists(Principal user) {
        return ResponseEntity.ok(service.findAllLists(user).stream()
                .map(ListInfoDTO::from)
                .collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<Long> createList(Principal user, @RequestBody ClientListDTO ClientList) {
        long listId = service.createList(ClientList.toClientList(), user);
        return ResponseEntity.ok(listId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientListDTO> getList(Principal user, @PathVariable int id) throws NotFoundException {
        ClientListDTO list = ClientListDTO.from(service.findListById(id, user));
        return ResponseEntity.ok(list);
    }

    @PostMapping("{id}/elements")
    public ResponseEntity<Long> addClient(Principal user, @RequestBody ClientDTO Client, @PathVariable(name = "id") int listId) throws NotFoundException {
        long ClientId = service.addClientToList(Client.toClient(), listId, user);
        return ResponseEntity.ok(ClientId);
    }

    @DeleteMapping("{id}/elements/{Client_id}")
    public ResponseEntity<Integer> removeClient(Principal user, @PathVariable(name = "id") long listId,
                                 @PathVariable(name = "Client_id") long ClientId) throws NotFoundException {
        service.deleteClient(ClientId, listId, user);
        return ResponseEntity.ok(1);
    }

    @GetMapping("{id}/elements/{Client_id}")
    public ResponseEntity<ClientDTO> findClientById(Principal user, @PathVariable(name = "id") long listId, @PathVariable(name = "Client_id") long ClientId) throws NotFoundException {
        Client client = service.findClientById(ClientId, listId, user);
        return ResponseEntity.ok(ClientDTO.from(client));
    }

    @GetMapping("{id}/size")
    public ResponseEntity<Integer> getListSize(Principal user, @PathVariable long id) throws NotFoundException {
        int size = service.getListSize(id, user);
        return ResponseEntity.ok(size);
    }

    @PutMapping("{id}/elements")
    public ResponseEntity<Long> addElementsToList(Principal user, @RequestBody List<ClientDTO> ClientsDTO, @PathVariable long id) {
        List<Client> clients = ClientsDTO.stream()
                .map(ClientDTO::toClient)
                .collect(Collectors.toList());

        long countOfAdded = service.addClientsToList(clients, id, user);
        return ResponseEntity.ok(countOfAdded);
    }

    @GetMapping("{id}/find")
    public ResponseEntity<Integer> getOccurrencesCount(Principal user, @PathVariable long id, @RequestParam(name = "element") String clientString) throws NotFoundException {
        int occurrencesCount = service.findOccurrencesCount(clientString, id, user);
        return ResponseEntity.ok(occurrencesCount);
    }

    @GetMapping("{id}/shuffle")
    public ResponseEntity<ClientListDTO> shuffle(Principal user, @PathVariable long id) throws NotFoundException {
        ClientListDTO shuffledList = ClientListDTO.from(service.shuffle(id, user));
        return ResponseEntity.ok(shuffledList);
    }

    @GetMapping("{id}/sort")
    public ResponseEntity<ClientListDTO> sort(Principal user, @PathVariable long id, @RequestParam(name = "comparator", required = false) String comparatorName) throws Exception {
        ClientListDTO sortedList = ClientListDTO.from(service.sort(id, comparatorName, user));
        return ResponseEntity.ok(sortedList);
    }

    @PostMapping("/comparator")
    public ResponseEntity<Long> createComparator(@RequestBody ClientComparator clientComparator) {
        long comparatorId = service.createComparator(clientComparator);
        return ResponseEntity.ok(comparatorId);
    }
}
