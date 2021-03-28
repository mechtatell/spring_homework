package ru.mechtatell.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mechtatell.DAO.Repos.ListRepository;
import ru.mechtatell.DAO.Repos.UserRepository;
import ru.mechtatell.Models.User;
import ru.mechtatell.Models.UserList;

import java.util.List;
import java.util.Optional;

@Component
public class UserListDAO {

    private final UserRepository userRepository;
    private final ListRepository listRepository;

    @Autowired
    public UserListDAO(UserRepository userRepository, ListRepository listRepository) {
        this.userRepository = userRepository;
        this.listRepository = listRepository;
    }

    public UserList createList(UserList list) {
        return listRepository.save(list);
    }

    public UserList getList(long id) {
        return listRepository.findById(id).orElse(null);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public List<UserList> getAll() {
        return (List<UserList>) listRepository.findAll();
    }

    public Optional<User> getUser(long id) {
        return userRepository.findById(id);
    }
}
