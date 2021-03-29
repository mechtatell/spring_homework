package ru.mechtatell.Services;

import groovy.lang.GroovyClassLoader;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mechtatell.Collections.AdvancedList;
import ru.mechtatell.Collections.MyCollection;
import ru.mechtatell.DAO.UserListDAO;
import ru.mechtatell.Models.UserComparator;
import ru.mechtatell.Models.User;
import ru.mechtatell.Models.UserList;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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

    public UserList sort(long id, String comparatorName) throws NotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        UserList userList = findListById(id);
        UserComparator userComparator = userListDAO.findByName(comparatorName);
        Comparator<User> comparator;

        if (Objects.isNull(userComparator)) {
            comparator = (o1, o2) -> (int) (o1.getSum() - o2.getSum());
        } else {
            comparator = loadComparator(userComparator.getCode());
        }

        MyCollection<User> myCollection = new MyCollection<>();
        userList.getUsers().forEach(myCollection::add);
        AdvancedList<User> sortedCollection  = myCollection.sort(comparator);

        List<User> sortedList = new ArrayList<>();
        for (int i = 0; i < sortedCollection.size(); i++) {
            sortedList.add(sortedCollection.get(i).get());
        }
        userList.setUsers(sortedList);

        return userList;
    }

    public long createComparator(UserComparator userComparator) {
        UserComparator savedUserComparator = userListDAO.createComparator(userComparator);
        return savedUserComparator.getId();
    }

    private Comparator<User> loadComparator(String code) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        GroovyClassLoader loader = new GroovyClassLoader();

        Object o = loader.parseClass(code).getDeclaredConstructor().newInstance();
        if (!(o instanceof Comparator)) {
            throw new InstantiationException("Cant read comporator");
        }

        return (Comparator<User>) o;
    }
}
