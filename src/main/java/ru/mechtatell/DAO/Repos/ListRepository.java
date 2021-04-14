package ru.mechtatell.DAO.Repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.mechtatell.Models.ClientList;
import ru.mechtatell.Models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListRepository extends CrudRepository<ClientList, Long> {
    List<ClientList> findAllByUser(User user);
    Optional<ClientList> findByIdAndUser(long id, User user);
}
