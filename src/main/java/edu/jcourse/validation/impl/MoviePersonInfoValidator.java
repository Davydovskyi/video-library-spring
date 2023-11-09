package edu.jcourse.validation.impl;

import edu.jcourse.dto.movieperson.MoviePersonCreateEditDto;
import edu.jcourse.dto.movieperson.MoviePersonFilter;
import edu.jcourse.service.MoviePersonService;
import edu.jcourse.validation.MoviePersonInfo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class MoviePersonInfoValidator implements ConstraintValidator<MoviePersonInfo, MoviePersonCreateEditDto> {

    private final MoviePersonService moviePersonService;

    @Override
    public boolean isValid(MoviePersonCreateEditDto value, ConstraintValidatorContext context) {
        if (isValid(value)) {
            MoviePersonFilter filter = MoviePersonFilter.builder()
                    .personId(value.personId())
                    .movieId(value.movieId())
                    .role(value.role())
                    .build();
            return moviePersonService.findByAllFields(filter).isEmpty();
        }
        return true;
    }

    private boolean isValid(MoviePersonCreateEditDto value) {
        return Objects.nonNull(value.personId()) &&
                Objects.nonNull(value.movieId()) &&
                Objects.nonNull(value.role());
    }
}