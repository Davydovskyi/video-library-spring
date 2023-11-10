package edu.jcourse.database.repository;

import edu.jcourse.database.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ReviewRepository extends JpaRepository<Review, Integer>,
        QuerydslPredicateExecutor<Review> {
}