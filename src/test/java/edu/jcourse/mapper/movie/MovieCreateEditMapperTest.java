package edu.jcourse.mapper.movie;

import edu.jcourse.database.entity.Genre;
import edu.jcourse.database.entity.Movie;
import edu.jcourse.dto.movie.MovieCreateEditDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MovieCreateEditMapperTest {

    private final MovieCreateEditMapper mapper = new MovieCreateEditMapper();

    @Test
    void mapForCreate() {
        Movie actualResult = mapper.map(buildMovieCreateEditDto());

        Movie expectedResult = buildMovie("title");

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void mapForUpdate() {
        Movie actualResult = mapper.map(buildMovieCreateEditDto(), buildMovie("title2"));

        Movie expectedResult = buildMovie("title");

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private MovieCreateEditDto buildMovieCreateEditDto() {
        return MovieCreateEditDto.builder()
                .title("title")
                .description("description")
                .releaseYear((short) 2022)
                .country("country")
                .genre(Genre.ACTION)
                .build();
    }

    private Movie buildMovie(String title) {
        return Movie.builder()
                .title(title)
                .description("description")
                .releaseYear((short) 2022)
                .country("country")
                .genre(Genre.ACTION)
                .build();
    }
}