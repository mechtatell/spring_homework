package ru.mechtatell.Repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.mechtatell.Models.ClientComparator;

@Repository
public interface ComparatorRepository extends CrudRepository<ClientComparator, Long> {

    ClientComparator findByName(String name);
}
