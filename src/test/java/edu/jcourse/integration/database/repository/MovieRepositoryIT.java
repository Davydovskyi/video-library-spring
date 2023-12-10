package edu.jcourse.integration.database.repository;

import edu.jcourse.database.entity.Movie;
import edu.jcourse.database.repository.MovieRepository;
import edu.jcourse.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class MovieRepositoryIT extends IntegrationTestBase {

    private static final Integer TENET_ID = 3;
    private final MovieRepository movieRepository;

    @Test
    void findById() {
        Optional<Movie> movie = movieRepository.findById(TENET_ID);

        assertThat(movie).isPresent();
        assertThat(movie.get().getReviews()).hasSize(2);
        assertThat(movie.get().getMoviePersons()).hasSize(1);
        assertThat(movie.get().getMoviePersons().get(0).getPerson()).isNotNull();
        assertThat(movie.get().getReviews().get(0).getUser()).isNotNull();
        assertThat(movie.get().getReviews().get(1).getUser()).isNotNull();
    }

    @Test
    void findByIdWhenNotFound() {
        Optional<Movie> movie = movieRepository.findById(100);

        assertThat(movie).isEmpty();
    }
}