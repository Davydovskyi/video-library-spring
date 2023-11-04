package edu.jcourse.validation.impl;

import edu.jcourse.dto.movie.MovieCreateEditDto;
import edu.jcourse.dto.movie.MovieFilter;
import edu.jcourse.service.MovieService;
import edu.jcourse.validation.MovieInfo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Objects;

@RequiredArgsConstructor
public class MovieInfoValidator implements ConstraintValidator<MovieInfo, MovieCreateEditDto> {
    private final MovieService movieService;

    @Override
    public boolean isValid(MovieCreateEditDto value, ConstraintValidatorContext context) {
        if (isValid(value)) {
            MovieFilter filter = MovieFilter.builder()
                    .title(value.title())
                    .releaseYear(value.releaseYear())
                    .country(value.country())
                    .genre(value.genre())
                    .build();
            return movieService.findByAllFields(filter).isEmpty();
        }
        return true;
    }

    private boolean isValid(MovieCreateEditDto value) {
        return StringUtils.hasText(value.title()) &&
                StringUtils.hasText(value.country()) &&
                Objects.nonNull(value.releaseYear()) &&
                Objects.nonNull(value.genre());
    }
}