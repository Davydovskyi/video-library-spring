package edu.jcourse.database.repository;

import edu.jcourse.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>,
        QuerydslPredicateExecutor<User>,
        RevisionRepository<User, Long, Integer> {

    default Optional<User> findByUsername(String username) {
        return findByEmail(username);
    }

    Optional<User> findByEmail(String email);
}