package edu.jcourse.validation.impl;

import edu.jcourse.database.entity.PersonRole;
import edu.jcourse.dto.movie.MovieReadDto;
import edu.jcourse.dto.movieperson.MoviePersonCreateEditDto;
import edu.jcourse.dto.movieperson.MoviePersonFilter;
import edu.jcourse.service.MoviePersonService;
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
class MoviePersonInfoValidatorTest {

    @Mock
    private MoviePersonService moviePersonService;
    @InjectMocks
    private MoviePersonInfoValidator moviePersonInfoValidator;

    static Stream<Arguments> getMoviePersonValidationArguments() {
        return Stream.of(
                Arguments.of(buildMoviePersonCreateEditDto(1, 1, PersonRole.ACTOR), false),
                Arguments.of(buildMoviePersonCreateEditDto(1, 1, PersonRole.COMPOSER), true),
                Arguments.of(buildMoviePersonCreateEditDto(1, null, PersonRole.ACTOR), true),
                Arguments.of(buildMoviePersonCreateEditDto(null, 1, PersonRole.ACTOR), true),
                Arguments.of(buildMoviePersonCreateEditDto(1, 1, null), true)
        );
    }

    private static MoviePersonCreateEditDto buildMoviePersonCreateEditDto(Integer personId, Integer movieId, PersonRole role) {
        return MoviePersonCreateEditDto.builder()
                .personId(personId)
                .movieId(movieId)
                .role(role)
                .build();
    }

    @ParameterizedTest
    @MethodSource("getMoviePersonValidationArguments")
    void isValidTest(MoviePersonCreateEditDto value, boolean expected) {
        MoviePersonFilter filter = new MoviePersonFilter(1, 1, PersonRole.ACTOR);
        lenient().doReturn(Optional.of(MovieReadDto.builder().build())).when(moviePersonService).findByAllFields(filter);

        assertThat(moviePersonInfoValidator.isValid(value, null)).isEqualTo(expected);
    }
}