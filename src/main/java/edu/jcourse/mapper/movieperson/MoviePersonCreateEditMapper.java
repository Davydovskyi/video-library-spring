package edu.jcourse.mapper.movieperson;

import edu.jcourse.database.entity.Movie;
import edu.jcourse.database.entity.MoviePerson;
import edu.jcourse.database.entity.Person;
import edu.jcourse.dto.movieperson.MoviePersonCreateEditDto;
import edu.jcourse.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class MoviePersonCreateEditMapper implements Mapper<MoviePersonCreateEditDto, MoviePerson> {
    @Override
    public MoviePerson map(MoviePersonCreateEditDto from) {
        return MoviePerson.builder()
                .movie(Movie.builder()
                        .id(from.movieId())
                        .build())
                .person(Person.builder()
                        .id(from.personId())
                        .build())
                .personRole(from.role())
                .build();
    }
}