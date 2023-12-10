package edu.jcourse.integration.service;

import edu.jcourse.database.entity.PersonRole;
import edu.jcourse.dto.movieperson.MoviePersonCreateEditDto;
import edu.jcourse.dto.movieperson.MoviePersonFilter;
import edu.jcourse.dto.movieperson.MoviePersonReadDto;
import edu.jcourse.integration.IntegrationTestBase;
import edu.jcourse.service.MoviePersonService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class MoviePersonServiceIT extends IntegrationTestBase {

    private final MoviePersonService moviePersonService;

    @Test
    void create() {
        MoviePersonCreateEditDto moviePerson = buildMoviePersonCreateEditDto();

        MoviePersonReadDto actualResult = moviePersonService.create(moviePerson);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.id()).isNotNull();
    }

    @Test
    void findByAllFields() {
        MoviePersonFilter filter = buildMoviePersonFilter(1);

        Optional<MoviePersonReadDto> actualResult = moviePersonService.findByAllFields(filter);

        assertThat(actualResult).isPresent();
    }

    @Test
    void findByAllFieldsWhenNotExists() {
        MoviePersonFilter filter = buildMoviePersonFilter(2);

        Optional<MoviePersonReadDto> actualResult = moviePersonService.findByAllFields(filter);

        assertThat(actualResult).isEmpty();
    }

    private MoviePersonCreateEditDto buildMoviePersonCreateEditDto() {
        return MoviePersonCreateEditDto.builder()
                .personId(2)
                .movieId(1)
                .role(PersonRole.ACTOR)
                .build();
    }

    private MoviePersonFilter buildMoviePersonFilter(Integer personId) {
        return MoviePersonFilter.builder()
                .personId(personId)
                .movieId(1)
                .role(PersonRole.COMPOSER)
                .build();
    }
}