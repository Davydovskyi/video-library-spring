package edu.jcourse.database.repository;

import edu.jcourse.database.entity.MoviePerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MoviePersonRepository extends JpaRepository<MoviePerson, Long>,
        QuerydslPredicateExecutor<MoviePerson> {
}