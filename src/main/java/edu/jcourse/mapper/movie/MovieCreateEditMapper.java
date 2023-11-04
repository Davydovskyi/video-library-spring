package edu.jcourse.mapper.movie;

import edu.jcourse.database.entity.Movie;
import edu.jcourse.dto.movie.MovieCreateEditDto;
import edu.jcourse.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class MovieCreateEditMapper implements Mapper<MovieCreateEditDto, Movie> {

    @Override
    public Movie map(MovieCreateEditDto from) {
        return Movie.builder()
                .releaseYear(from.releaseYear())
                .country(from.country())
                .genre(from.genre())
                .title(from.title())
                .description(from.description())
                .build();
    }
}