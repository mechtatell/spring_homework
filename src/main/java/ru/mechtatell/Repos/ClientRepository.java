package ru.mechtatell.Repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.mechtatell.Models.Client;

import java.util.List;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
}
