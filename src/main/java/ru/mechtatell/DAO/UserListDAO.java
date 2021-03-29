package ru.mechtatell.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mechtatell.DAO.Repos.ComparatorRepository;
import ru.mechtatell.DAO.Repos.ListRepository;
import ru.mechtatell.DAO.Repos.UserRepository;
import ru.mechtatell.Models.UserComparator;
import ru.mechtatell.Models.User;
import ru.mechtatell.Models.UserList;

import java.util.List;
import java.util.Optional;

@Component
public class UserListDAO {

    private final UserRepository userRepository;
    private final ListRepository listRepository;
    private final ComparatorRepository comparatorRepository;

    @Autowired
    public UserListDAO(UserRepository userRepository, ListRepository listRepository,
                       ComparatorRepository comparatorRepository) {
        this.userRepository = userRepository;
        this.listRepository = listRepository;
        this.comparatorRepository = comparatorRepository;
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

    public UserComparator createComparator(UserComparator userComparator) {
        return comparatorRepository.save(userComparator);
    }

    public UserComparator findByName(String name) {
        return comparatorRepository.findByName(name);
    }
}
