package ru.mechtatell.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mechtatell.DAO.Interfaces.ListDAO;
import ru.mechtatell.DAO.Repos.ListRepository;
import ru.mechtatell.Models.ClientList;
import ru.mechtatell.Models.User;

import java.util.List;
import java.util.Optional;

@Service
public class ListDAOImpl implements ListDAO {

    private final ListRepository listRepository;

    @Autowired
    public ListDAOImpl(ListRepository listRepository) {
        this.listRepository = listRepository;
    }

    @Override
    public List<ClientList> findAllByUser(User user) {
        return listRepository.findAllByUser(user);
    }

    @Override
    public Optional<ClientList> findById(long id, User user) {
        return listRepository.findByIdAndUser(id, user);
    }

    @Override
    public Optional<ClientList> findById(long id) {
        return listRepository.findById(id);
    }

    @Override
    public List<ClientList> findAll() {
        return (List<ClientList>) listRepository.findAll();
    }

    @Override
    public ClientList save(ClientList item) {
        return listRepository.save(item);
    }

    @Override
    public void delete(ClientList item) {
        listRepository.delete(item);
    }
}
