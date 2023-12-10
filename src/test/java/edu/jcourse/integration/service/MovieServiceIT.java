package edu.jcourse.integration.service;

import edu.jcourse.database.entity.Genre;
import edu.jcourse.dto.movie.MovieCreateEditDto;
import edu.jcourse.dto.movie.MovieFilter;
import edu.jcourse.dto.movie.MovieReadDto;
import edu.jcourse.integration.IntegrationTestBase;
import edu.jcourse.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class MovieServiceIT extends IntegrationTestBase {

    private static final Integer MOVIE_ID = 1;
    private final MovieService movieService;

    static Stream<Arguments> getFindAllMovieArguments() {
        return Stream.of(
                Arguments.of(buildMovieFilter(null, null, null, null), buildPageable(0, 10), 3),
                Arguments.of(buildMovieFilter(null, null, null, null), buildPageable(0, 2), 2),
                Arguments.of(buildMovieFilter(null, null, null, null), buildPageable(2, 10), 0),
                Arguments.of(buildMovieFilter("The Dark Knight", null, null, null), buildPageable(0, 10), 1),
                Arguments.of(buildMovieFilter("t", null, null, null), buildPageable(0, 10), 3),
                Arguments.of(buildMovieFilter("t", "USA", null, null), buildPageable(0, 10), 2),
                Arguments.of(buildMovieFilter("t", "USA", (short) 2020, null), buildPageable(0, 10), 0),
                Arguments.of(buildMovieFilter("t", "USA", (short) 2008, Genre.ACTION), buildPageable(0, 10), 1),
                Arguments.of(buildMovieFilter(null, null, (short) 2008, null), buildPageable(0, 10), 1));
    }

    private static MovieFilter buildMovieFilter(String title, String country, Short releaseYear, Genre genre) {
        return MovieFilter.builder()
                .title(title)
                .releaseYear(releaseYear)
                .country(country)
                .genre(genre)
                .build();
    }

    private static Pageable buildPageable(int page, int size) {
        return Pageable.ofSize(size).withPage(page);
    }

    @ParameterizedTest
    @MethodSource("getFindAllMovieArguments")
    void findAll(MovieFilter filter, Pageable pageable, int expectedCount) {
        assertThat(movieService.findAll(filter, pageable)).hasSize(expectedCount);
    }

    @Test
    void findById() {
        assertThat(movieService.findById(MOVIE_ID)).isPresent();
    }

    @Test
    void findByIdWhenNotFound() {
        assertThat(movieService.findById(100)).isNotPresent();
    }

    @Test
    void create() {
        MovieCreateEditDto movie = buildMovieCreateEditDto("title");

        MovieReadDto actualResult = movieService.create(movie);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.id()).isNotNull();
    }

    @Test
    void findByAllFields() {
        assertThat(movieService.findByAllFields(buildMovieFilter("Tenet", "United Kingdom", (short) 2020, Genre.SCIENCE_FICTION))).isPresent();
    }

    @Test
    void findByAllFieldsWhenNotFound() {
        assertThat(movieService.findByAllFields(buildMovieFilter("Tenet", "United Kingdom", (short) 2021, Genre.SCIENCE_FICTION))).isNotPresent();
    }

    @Test
    void update() {
        MovieCreateEditDto movie = buildMovieCreateEditDto("title");
        MovieCreateEditDto editDto = buildMovieCreateEditDto("new title");
        MovieReadDto actualResult = movieService.create(movie);

        Optional<MovieReadDto> updated = movieService.update(actualResult.id(), editDto);

        assertThat(updated).isPresent();
        assertThat(updated.get().title()).isEqualTo("new title");
    }

    @Test
    void updateWhenNotFound() {
        MovieCreateEditDto editDto = buildMovieCreateEditDto("new title");

        Optional<MovieReadDto> updated = movieService.update(100, editDto);

        assertThat(updated).isEmpty();
    }

    @Test
    void delete() {
        assertThat(movieService.delete(MOVIE_ID)).isTrue();
    }

    @Test
    void deleteWhenNotFound() {
        assertThat(movieService.delete(100)).isFalse();
    }

    private MovieCreateEditDto buildMovieCreateEditDto(String title) {
        return MovieCreateEditDto.builder()
                .title(title)
                .country("country")
                .releaseYear((short) 2000)
                .genre(Genre.ACTION)
                .description("description")
                .build();
    }
}