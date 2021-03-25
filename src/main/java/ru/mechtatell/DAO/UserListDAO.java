package ru.mechtatell.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mechtatell.DAO.Repos.ListRepository;
import ru.mechtatell.DAO.Repos.UserRepository;
import ru.mechtatell.Models.User;
import ru.mechtatell.Models.UserList;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserListDAO {

    private final UserRepository userRepository;
    private final ListRepository listRepository;

    @Autowired
    public UserListDAO(UserRepository userRepository, ListRepository listRepository) {
        this.userRepository = userRepository;
        this.listRepository = listRepository;
    }

    public long add(UserList list) {
        UserList savedList = listRepository.save(list);
        return savedList.getId();
    }

    public UserList get(long id) {
        return listRepository.findById(id).orElse(null);
    }

    public User addUser(User user, long listId) {
        user.setUserList(get(listId));
        return userRepository.save(user);
    }

    public boolean delete(long id) {
        try {
            listRepository.delete(get(id));
        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    public void deleteUser(long userId) {

    }

    public List<UserList> getAll() {
        return (List<UserList>) listRepository.findAll();
    }
}
