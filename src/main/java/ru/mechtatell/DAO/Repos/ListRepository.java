package ru.mechtatell.DAO.Repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.mechtatell.Models.UserList;

@Repository
public interface ListRepository extends CrudRepository<UserList, Long> {
}
