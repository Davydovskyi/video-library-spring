package edu.jcourse.service;

import com.querydsl.core.types.Predicate;
import edu.jcourse.database.entity.Review;
import edu.jcourse.database.querydsl.QPredicates;
import edu.jcourse.database.repository.ReviewRepository;
import edu.jcourse.dto.review.ReviewCreateEditDto;
import edu.jcourse.dto.review.ReviewFilter;
import edu.jcourse.dto.review.ReviewReadDto;
import edu.jcourse.mapper.review.ReviewCreateEditMapper;
import edu.jcourse.mapper.review.ReviewReadMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static edu.jcourse.database.entity.QReview.review;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewReadMapper reviewReadMapper;
    @Mock
    private ReviewCreateEditMapper reviewCreateEditMapper;
    @InjectMocks
    private ReviewService reviewService;

    @Test
    void findAllByUserId() {
        List<Review> reviews = List.of(buildReview(1), buildReview(2));
        doReturn(reviews).when(reviewRepository).findAllByUserId(any());
        doReturn(buildReviewReadDto(1)).when(reviewReadMapper).fullMap(reviews.get(0));
        doReturn(buildReviewReadDto(2)).when(reviewReadMapper).fullMap(reviews.get(1));
        List<ReviewReadDto> expectedResult = List.of(buildReviewReadDto(1), buildReviewReadDto(2));

        List<ReviewReadDto> actualResult = reviewService.findAllByUserId(1L);

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(reviewRepository).findAllByUserId(1L);
        verify(reviewReadMapper, times(2)).fullMap(any());
        verifyNoMoreInteractions(reviewRepository, reviewReadMapper);
    }

    @Test
    void findAllByUserIdWhenReviewNotFound() {
        doReturn(List.of()).when(reviewRepository).findAllByUserId(any());

        List<ReviewReadDto> actualResult = reviewService.findAllByUserId(1L);

        assertThat(actualResult).isEmpty();
        verify(reviewRepository).findAllByUserId(1L);
        verifyNoMoreInteractions(reviewRepository);
        verifyNoInteractions(reviewReadMapper);
    }

    @Test
    void findByAllFields() {
        Review review = buildReview(1);
        ReviewFilter filter = buildReviewFilter();
        Predicate predicate = buildPredicate();
        ReviewReadDto reviewReadDto = buildReviewReadDto(1);
        doReturn(Optional.of(review)).when(reviewRepository).findOne(any(Predicate.class));
        doReturn(reviewReadDto).when(reviewReadMapper).map(any());
        ReviewReadDto expectedResult = buildReviewReadDto(1);

        Optional<ReviewReadDto> actualResult = reviewService.findByAllFields(filter);

        assertThat(actualResult).contains(expectedResult);
        verify(reviewRepository).findOne(predicate);
        verify(reviewReadMapper).map(review);
        verifyNoMoreInteractions(reviewRepository, reviewReadMapper);
    }

    @Test
    void findByAllFieldsWhenReviewNotFound() {
        ReviewFilter filter = buildReviewFilter();
        Predicate predicate = buildPredicate();
        doReturn(Optional.empty()).when(reviewRepository).findOne(any(Predicate.class));

        Optional<ReviewReadDto> actualResult = reviewService.findByAllFields(filter);

        assertThat(actualResult).isEmpty();
        verify(reviewRepository).findOne(predicate);
        verifyNoMoreInteractions(reviewRepository);
        verifyNoInteractions(reviewReadMapper);
    }

    @Test
    void create() {
        Review review = buildReview(1);
        ReviewCreateEditDto reviewCreateEditDto = buildReviewCreateEditDto();
        ReviewReadDto reviewReadDto = buildReviewReadDto(1);
        doReturn(review).when(reviewCreateEditMapper).map(any());
        doReturn(review).when(reviewRepository).save(any());
        doReturn(reviewReadDto).when(reviewReadMapper).map(any());

        ReviewReadDto actualResult = reviewService.create(reviewCreateEditDto);

        assertThat(actualResult).isEqualTo(reviewReadDto);
        verify(reviewCreateEditMapper).map(reviewCreateEditDto);
        verify(reviewRepository).save(review);
        verify(reviewReadMapper).map(review);
        verifyNoMoreInteractions(reviewRepository, reviewReadMapper, reviewCreateEditMapper);
    }

    @Test
    void createWhenReviewNotFound() {
        ReviewCreateEditDto reviewCreateEditDto = buildReviewCreateEditDto();
        Review review = buildReview(1);
        doReturn(review).when(reviewCreateEditMapper).map(any());
        doReturn(null).when(reviewRepository).save(any());

        assertThrowsExactly(NoSuchElementException.class, () -> reviewService.create(reviewCreateEditDto));
        verify(reviewCreateEditMapper).map(reviewCreateEditDto);
        verify(reviewRepository).save(review);
        verifyNoMoreInteractions(reviewCreateEditMapper, reviewRepository);
        verifyNoInteractions(reviewReadMapper);
    }

    private Review buildReview(Integer id) {
        return Review.builder()
                .id(id)
                .rate((short) 5)
                .reviewText("test")
                .build();
    }

    private ReviewReadDto buildReviewReadDto(Integer id) {
        return ReviewReadDto.builder()
                .id(id)
                .rate((short) 5)
                .reviewText("test")
                .build();
    }

    private ReviewFilter buildReviewFilter() {
        return ReviewFilter.builder()
                .userId(1L)
                .movieId(1)
                .build();
    }

    private ReviewCreateEditDto buildReviewCreateEditDto() {
        return ReviewCreateEditDto.builder()
                .userId(1L)
                .movieId(1)
                .rate((short) 5)
                .reviewText("test")
                .build();
    }

    private Predicate buildPredicate() {
        return QPredicates.builder()
                .add(1, review.movie.id::eq)
                .add(1L, review.user.id::eq)
                .buildAnd();
    }
}