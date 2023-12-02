package edu.jcourse.validation.impl;

import edu.jcourse.database.entity.Genre;
import edu.jcourse.dto.movie.MovieCreateEditDto;
import edu.jcourse.dto.movie.MovieFilter;
import edu.jcourse.dto.movie.MovieReadDto;
import edu.jcourse.service.MovieService;
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
class MovieInfoValidatorTest {

    @Mock
    private MovieService movieService;
    @InjectMocks
    private MovieInfoValidator validator;

    static Stream<Arguments> getMovieValidationArguments() {
        return Stream.of(
                Arguments.of(buildMovieCreateEditDto("title", "country", (short) 2022, Genre.ACTION), false),
                Arguments.of(buildMovieCreateEditDto("title", "country", (short) 2022, Genre.DRAMA), true),
                Arguments.of(buildMovieCreateEditDto("", "country", (short) 2022, Genre.ACTION), true),
                Arguments.of(buildMovieCreateEditDto("title", "", (short) 2022, Genre.ACTION), true),
                Arguments.of(buildMovieCreateEditDto("title", "country", null, Genre.ACTION), true),
                Arguments.of(buildMovieCreateEditDto("title", "country", (short) 2022, null), true)
        );
    }

    private static MovieCreateEditDto buildMovieCreateEditDto(String title, String country, Short releaseYear, Genre genre) {
        return MovieCreateEditDto.builder()
                .title(title)
                .country(country)
                .releaseYear(releaseYear)
                .genre(genre)
                .build();
    }

    @ParameterizedTest
    @MethodSource("getMovieValidationArguments")
    void isValid(MovieCreateEditDto dto, boolean expected) {
        MovieFilter filter = new MovieFilter("title", Genre.ACTION, (short) 2022, "country", null);
        lenient().when(movieService.findByAllFields(filter)).thenReturn(Optional.of(MovieReadDto.builder().build()));

        assertThat(validator.isValid(dto, null)).isEqualTo(expected);
    }
}