package ru.mechtatell.Services;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mechtatell.Collections.AdvancedList;
import ru.mechtatell.Collections.MyCollection;
import ru.mechtatell.DAO.UserListDAO;
import ru.mechtatell.Models.User;
import ru.mechtatell.Models.UserList;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UsersListService {

    private final UserListDAO userListDAO;

    @Autowired
    public UsersListService(UserListDAO userListDAO) {
        this.userListDAO = userListDAO;
    }

    public long createList(UserList list) {
        UserList savedList = userListDAO.createList(list);
        return savedList.getId();
    }

    public List<UserList> findAllLists() {
        return userListDAO.getAll();
    }

    public UserList findListById(long id) throws NotFoundException {
        UserList userList = userListDAO.getList(id);

        if (userList == null) {
            throw new NotFoundException("Cant find list with id " + id);
        }

        return userList;
    }

    public int getListSize(long id) throws NotFoundException {
        UserList userList = findListById(id);
        return userList.getUsers().size();
    }

    @Transactional
    public long addUserToList(User user, long listId) throws NotFoundException {
        UserList userList = findListById(listId);
        user.setUserList(userList);

        User savedUser = userListDAO.addUser(user);
        return savedUser.getId();
    }

    public User findUserById(long userId, long listId) throws NotFoundException {
        if (userListDAO.getUser(userId).isEmpty()) {
            throw new NotFoundException("Cant find user with id " + userId);
        }

        UserList userList = findListById(listId);
        User user = userListDAO.getUser(userId).get();

        if (!userList.getUsers().contains(user)) {
            throw new NotFoundException("User with id " + userId + " is in another list");
        }

        return user;
    }

    public void deleteUser(long userId, long listId) throws NotFoundException {
        User user = findUserById(userId, listId);
        user.getUserList().getUsers().remove(user);
        userListDAO.deleteUser(user);
    }

    public long addUsersToList(List<User> users, long listId) {
        UserList userList;
        try {
            userList = findListById(listId);
        } catch (NotFoundException e) {
            userList = new UserList();
            userList.setName("New List");
            userListDAO.createList(userList);
        }

        for (User user : users) {
            user.setUserList(userList);
            userListDAO.addUser(user);
        }

        return users.size();
    }

    public int findOccurrencesCount(User user, long listId) throws NotFoundException {
        UserList userList = findListById(listId);

        if (!userList.getUsers().contains(user)) {
            throw new NotFoundException("User is in another list");
        }

        return (int) userList.getUsers().stream()
                .filter(e -> e.equals(user))
                .count();
    }

    public UserList shuffle(long id) throws NotFoundException {
        UserList userList = findListById(id);

        MyCollection<User> myCollection = new MyCollection<>();
        userList.getUsers().forEach(myCollection::add);
        AdvancedList<User> shuffledCollection  = myCollection.shuffle();

        List<User> shuffledList = new ArrayList<>();
        for (int i = 0; i < shuffledCollection.size(); i++) {
            shuffledList.add(shuffledCollection.get(i).get());
        }
        userList.setUsers(shuffledList);

        return userList;
    }

    public UserList sort(long id) throws NotFoundException {
        UserList userList = findListById(id);

        MyCollection<User> myCollection = new MyCollection<>();
        userList.getUsers().forEach(myCollection::add);
        AdvancedList<User> sortedCollection  = myCollection.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return (int) (o1.getSum() - o2.getSum());
            }
        });

        List<User> sortedList = new ArrayList<>();
        for (int i = 0; i < sortedCollection.size(); i++) {
            sortedList.add(sortedCollection.get(i).get());
        }
        userList.setUsers(sortedList);

        return userList;
    }
}
