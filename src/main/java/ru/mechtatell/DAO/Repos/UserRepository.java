package ru.mechtatell.DAO.Repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.mechtatell.Models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
