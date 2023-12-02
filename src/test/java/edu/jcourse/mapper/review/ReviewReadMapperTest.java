package edu.jcourse.mapper.review;

import edu.jcourse.database.entity.Movie;
import edu.jcourse.database.entity.Review;
import edu.jcourse.database.entity.User;
import edu.jcourse.dto.movie.MovieReadDto;
import edu.jcourse.dto.review.ReviewReadDto;
import edu.jcourse.dto.user.UserReadDto;
import edu.jcourse.mapper.movie.MovieReadMapper;
import edu.jcourse.mapper.user.UserReadMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewReadMapperTest {

    @Mock
    private UserReadMapper userReadMapper;
    @Mock
    private MovieReadMapper movieReadMapper;
    @InjectMocks
    private ReviewReadMapper reviewReadMapper;

    @Test
    void map() {
        Review review = buildReview();
        ReviewReadDto expectedResult = buildReviewReadDto(null, null);

        ReviewReadDto actualResult = reviewReadMapper.map(review);

        assertThat(actualResult).isEqualTo(expectedResult);
        verifyNoInteractions(userReadMapper, movieReadMapper);
    }

    @Test
    void fullMap() {
        Review review = buildReview();
        doReturn(UserReadDto.builder().build()).when(userReadMapper).map(any());
        doReturn(MovieReadDto.builder().build()).when(movieReadMapper).map(any());
        ReviewReadDto expectedResult = buildReviewReadDto(UserReadDto.builder().build(), MovieReadDto.builder().build());

        ReviewReadDto actualResult = reviewReadMapper.fullMap(review);

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(userReadMapper).map(any());
        verify(movieReadMapper).map(any());
    }

    private Review buildReview() {
        return Review.builder()
                .id(1)
                .reviewText("test")
                .rate((short) 1)
                .movie(new Movie())
                .user(new User())
                .build();
    }

    private ReviewReadDto buildReviewReadDto(UserReadDto user, MovieReadDto movie) {
        return ReviewReadDto.builder()
                .id(1)
                .reviewText("test")
                .rate((short) 1)
                .user(user)
                .movie(movie)
                .build();
    }
}