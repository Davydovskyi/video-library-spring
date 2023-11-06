package edu.jcourse.mapper.movie;

import edu.jcourse.database.entity.Movie;
import edu.jcourse.dto.movie.MovieCreateEditDto;
import edu.jcourse.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class MovieCreateEditMapper implements Mapper<MovieCreateEditDto, Movie> {

    @Override
    public Movie map(MovieCreateEditDto from) {
        Movie movie = new Movie();
        copy(from, movie);
        return movie;
    }

    @Override
    public Movie map(MovieCreateEditDto from, Movie to) {
        copy(from, to);
        return to;
    }

    private void copy(MovieCreateEditDto from, Movie to) {
        to.setReleaseYear(from.releaseYear());
        to.setCountry(from.country());
        to.setGenre(from.genre());
        to.setTitle(from.title());
        to.setDescription(from.description());
    }
}