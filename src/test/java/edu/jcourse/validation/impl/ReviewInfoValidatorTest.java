package edu.jcourse.validation.impl;

import edu.jcourse.dto.review.ReviewCreateEditDto;
import edu.jcourse.dto.review.ReviewFilter;
import edu.jcourse.dto.review.ReviewReadDto;
import edu.jcourse.service.ReviewService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class ReviewInfoValidatorTest {

    @Mock
    private ReviewService reviewService;
    @InjectMocks
    private ReviewInfoValidator reviewInfoValidator;

    static Stream<Arguments> getReviewValidationArguments() {
        return Stream.of(
                Arguments.of(buildReviewCreateEditDto(1, 1L, (short) 4, "test"), false),
                Arguments.of(buildReviewCreateEditDto(2, 1L, (short) 1, "test"), true),
                Arguments.of(buildReviewCreateEditDto(1, 2L, (short) 1, "test"), true),
                Arguments.of(buildReviewCreateEditDto(null, 1L, (short) 2, "test"), true),
                Arguments.of(buildReviewCreateEditDto(1, null, (short) 1, "test1"), true),
                Arguments.of(buildReviewCreateEditDto(1, 1L, null, ""), true),
                Arguments.of(buildReviewCreateEditDto(1, 1L, (short) 1, null), true),
                Arguments.of(buildReviewCreateEditDto(1, 1L, (short) 1, ""), true)
        );
    }

    private static ReviewCreateEditDto buildReviewCreateEditDto(Integer movieId, Long userId, Short rate, String reviewText) {
        return ReviewCreateEditDto.builder()
                .movieId(movieId)
                .userId(userId)
                .rate(rate)
                .reviewText(reviewText)
                .build();
    }

    @ParameterizedTest
    @MethodSource("getReviewValidationArguments")
    void isValid(ReviewCreateEditDto value, boolean expectedResult) {
        ReviewFilter filter = new ReviewFilter(1L, 1);
        lenient().doReturn(Optional.of(ReviewReadDto.builder().build())).when(reviewService).findByAllFields(filter);

        assertThat(reviewInfoValidator.isValid(value, null)).isEqualTo(expectedResult);
    }
}