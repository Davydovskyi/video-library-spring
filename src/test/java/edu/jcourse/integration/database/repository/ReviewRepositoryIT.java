package edu.jcourse.integration.database.repository;

import edu.jcourse.database.entity.Review;
import edu.jcourse.database.repository.ReviewRepository;
import edu.jcourse.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class ReviewRepositoryIT extends IntegrationTestBase {

    private static final Long USER_ID = 1L;
    private final ReviewRepository reviewRepository;

    @Test
    void findAllByUserId() {
        List<Review> reviews = reviewRepository.findAllByUserId(USER_ID);

        assertThat(reviews).hasSize(2);
        assertThat(reviews.get(0).getUser()).isNotNull();
        assertThat(reviews.get(0).getMovie()).isNotNull();
    }

    @Test
    void findAllByUserIdWhenNoReviews() {
        List<Review> reviews = reviewRepository.findAllByUserId(100L);

        assertThat(reviews).isEmpty();
    }
}