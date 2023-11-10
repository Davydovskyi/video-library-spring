package edu.jcourse.database.repository;

import edu.jcourse.database.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer>,
        QuerydslPredicateExecutor<Review> {

    @EntityGraph(attributePaths = {"movie", "user"})
    List<Review> findAllByUserId(Long userId);
}