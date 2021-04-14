package ru.mechtatell.DAO.Repos;

import org.springframework.data.repository.CrudRepository;
import ru.mechtatell.Models.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByLogin(String login);
    User findByLoginAndPassword(String login, String password);
}
