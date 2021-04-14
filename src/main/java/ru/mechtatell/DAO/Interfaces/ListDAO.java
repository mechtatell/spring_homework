package ru.mechtatell.DAO.Interfaces;

import ru.mechtatell.Models.ClientList;
import ru.mechtatell.Models.User;

import java.util.List;
import java.util.Optional;

public interface ListDAO extends DAO<ClientList> {
    List<ClientList> findAllByUser(User user);
    Optional<ClientList> findById(long id, User user);
}
