package ru.mechtatell.DAO.Repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.mechtatell.Models.UserComparator;

@Repository
public interface ComparatorRepository extends CrudRepository<UserComparator, Long> {

    UserComparator findByName(String name);
}
