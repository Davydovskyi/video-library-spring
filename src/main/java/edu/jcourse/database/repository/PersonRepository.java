package edu.jcourse.database.repository;

import edu.jcourse.database.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {
}