package ru.mechtatell.DAO.Interfaces;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    Optional<T> findById(long id);
    List<T> findAll();
    T save(T item);
    void delete(T item);
}
