package edu.jcourse.mapper.review;

import edu.jcourse.database.entity.Movie;
import edu.jcourse.database.entity.Review;
import edu.jcourse.database.entity.User;
import edu.jcourse.dto.review.ReviewCreateEditDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewCreateEditMapperTest {

    private final ReviewCreateEditMapper mapper = new ReviewCreateEditMapper();

    @Test
    void map() {
        ReviewCreateEditDto reviewCreateEditDto = buildReviewCreateEditDto();
        Review expectedResult = buildReview();

        Review actualResult = mapper.map(reviewCreateEditDto);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void defaultFullMap() {
        Review actualResult = mapper.fullMap(buildReviewCreateEditDto());

        assertThat(actualResult).isNull();
    }

    @Test
    void defaultMap() {
        ReviewCreateEditDto createEditDto = buildReviewCreateEditDto();
        Review expectedResult = buildReview();

        Review actualResult = mapper.map(buildReviewCreateEditDto(), expectedResult);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private ReviewCreateEditDto buildReviewCreateEditDto() {
        return ReviewCreateEditDto.builder()
                .userId(1L)
                .movieId(1)
                .reviewText("test")
                .rate((short) 1)
                .build();
    }

    private Review buildReview() {
        return Review.builder()
                .user(User.builder()
                        .id(1L)
                        .build())
                .movie(Movie.builder()
                        .id(1)
                        .build())
                .reviewText("test")
                .rate((short) 1)
                .build();
    }
}