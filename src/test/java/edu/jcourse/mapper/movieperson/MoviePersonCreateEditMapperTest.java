package edu.jcourse.mapper.movieperson;

import edu.jcourse.database.entity.Movie;
import edu.jcourse.database.entity.MoviePerson;
import edu.jcourse.database.entity.Person;
import edu.jcourse.database.entity.PersonRole;
import edu.jcourse.dto.movieperson.MoviePersonCreateEditDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MoviePersonCreateEditMapperTest {

    private final MoviePersonCreateEditMapper mapper = new MoviePersonCreateEditMapper();

    @Test
    void map() {
        MoviePersonCreateEditDto dto = buildMoviePersonCreateEditDto();
        MoviePerson expectedResult = buildMoviePerson();

        MoviePerson actualResult = mapper.map(dto);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private MoviePersonCreateEditDto buildMoviePersonCreateEditDto() {
        return MoviePersonCreateEditDto.builder()
                .movieId(1)
                .personId(1)
                .role(PersonRole.ACTOR)
                .build();
    }

    private MoviePerson buildMoviePerson() {
        return MoviePerson.builder()
                .movie(Movie.builder()
                        .id(1)
                        .build())
                .person(Person.builder()
                        .id(1)
                        .build())
                .personRole(PersonRole.ACTOR)
                .build();
    }
}