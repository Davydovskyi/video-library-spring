package edu.jcourse.database.repository;

import edu.jcourse.database.entity.Movie;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Integer>,
        QuerydslPredicateExecutor<Movie>,
        RevisionRepository<Movie, Integer, Integer> {

    @EntityGraph(attributePaths = {
            "moviePersons",
            "reviews",
            "moviePersons.person",
            "reviews.user"})
    @Override
    Optional<Movie> findById(Integer id);
}