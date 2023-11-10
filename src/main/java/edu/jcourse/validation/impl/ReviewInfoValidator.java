package edu.jcourse.validation.impl;

import edu.jcourse.dto.review.ReviewCreateEditDto;
import edu.jcourse.dto.review.ReviewFilter;
import edu.jcourse.service.ReviewService;
import edu.jcourse.validation.ReviewInfo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Objects;

@RequiredArgsConstructor
public class ReviewInfoValidator implements ConstraintValidator<ReviewInfo, ReviewCreateEditDto> {

    private final ReviewService reviewService;

    @Override
    public boolean isValid(ReviewCreateEditDto value, ConstraintValidatorContext context) {
        if (isValid(value)) {
            ReviewFilter filter = ReviewFilter.builder()
                    .movieId(value.movieId())
                    .userId(value.userId())
                    .build();
            return reviewService.findByAllFields(filter).isEmpty();
        }
        return true;
    }

    private boolean isValid(ReviewCreateEditDto value) {
        return Objects.nonNull(value.movieId()) &&
                Objects.nonNull(value.rate()) &&
                Objects.nonNull(value.userId()) &&
                StringUtils.hasText(value.reviewText());
    }
}