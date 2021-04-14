package ru.mechtatell.DAO.Repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.mechtatell.Models.Client;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
}
