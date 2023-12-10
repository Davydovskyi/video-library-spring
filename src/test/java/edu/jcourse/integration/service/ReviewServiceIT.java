package edu.jcourse.integration.service;

import edu.jcourse.dto.review.ReviewCreateEditDto;
import edu.jcourse.dto.review.ReviewFilter;
import edu.jcourse.dto.review.ReviewReadDto;
import edu.jcourse.integration.IntegrationTestBase;
import edu.jcourse.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class ReviewServiceIT extends IntegrationTestBase {

    private static final Long USER_ID = 1L;
    private final ReviewService reviewService;

    @Test
    void findAllByUserId() {
        assertThat(reviewService.findAllByUserId(USER_ID)).hasSize(2);
    }

    @Test
    void findAllByUserIdWhenNoReviews() {
        assertThat(reviewService.findAllByUserId(20L)).isEmpty();
    }

    @Test
    void findByAllFields() {
        assertThat(reviewService.findByAllFields(buildReviewFilter(1))).isPresent();
    }

    @Test
    void findByAllFieldsWhenNoReviews() {
        assertThat(reviewService.findByAllFields(buildReviewFilter(3))).isEmpty();
    }

    @Test
    void create() {
        ReviewCreateEditDto createEditDto = buildReviewCreateEditDto();

        ReviewReadDto actualResult = reviewService.create(createEditDto);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.id()).isNotNull();
    }

    private ReviewFilter buildReviewFilter(Integer movieId) {
        return ReviewFilter.builder()
                .userId(USER_ID)
                .movieId(movieId)
                .build();
    }

    private ReviewCreateEditDto buildReviewCreateEditDto() {
        return ReviewCreateEditDto.builder()
                .movieId(2)
                .userId(3L)
                .rate((short) 5)
                .reviewText("test")
                .build();
    }
}