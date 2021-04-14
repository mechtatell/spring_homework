package ru.mechtatell.DAO.Interfaces;

import ru.mechtatell.Models.User;

import java.util.Optional;

public interface AuthDAO extends DAO<User> {
    Optional<User> findByName(String name);
}
