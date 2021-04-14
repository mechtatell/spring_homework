package ru.mechtatell.DAO.Interfaces;

import ru.mechtatell.Models.Client;

import java.util.Optional;

public interface ClientDAO extends DAO<Client> {
    Optional<Client> findByName(String name);
}
