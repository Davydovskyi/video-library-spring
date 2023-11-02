package edu.jcourse.mapper.movie;

import edu.jcourse.database.entity.Movie;
import edu.jcourse.dto.movie.MovieReadDto;
import edu.jcourse.dto.movieperson.MoviePersonReadDto;
import edu.jcourse.dto.review.ReviewReadDto;
import edu.jcourse.mapper.Mapper;
import edu.jcourse.mapper.movieperson.MoviePersonReadMapper;
import edu.jcourse.mapper.review.ReviewReadMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieReadMapper implements Mapper<Movie, MovieReadDto> {

    private final MoviePersonReadMapper moviePersonReadMapper;
    private final ReviewReadMapper reviewReadMapper;

    public MovieReadMapper(MoviePersonReadMapper moviePersonReadMapper, @Lazy ReviewReadMapper reviewReadMapper) {
        this.moviePersonReadMapper = moviePersonReadMapper;
        this.reviewReadMapper = reviewReadMapper;
    }

    @Override
    public MovieReadDto map(Movie from) {
        return MovieReadDto.builder()
                .id(from.getId())
                .title(from.getTitle())
                .releaseYear(from.getReleaseYear())
                .country(from.getCountry())
                .genre(from.getGenre())
                .description(from.getDescription())
                .build();
    }

    @Override
    public MovieReadDto fullMap(Movie from) {
        List<MoviePersonReadDto> moviePersons = from.getMoviePersons().stream()
                .map(moviePersonReadMapper::map)
                .toList();

        List<ReviewReadDto> reviews = from.getReviews().stream()
                .map(reviewReadMapper::map)
                .toList();

        return MovieReadDto.builder()
                .id(from.getId())
                .title(from.getTitle())
                .releaseYear(from.getReleaseYear())
                .country(from.getCountry())
                .genre(from.getGenre())
                .description(from.getDescription())
                .moviePersons(moviePersons)
                .reviews(reviews)
                .build();
    }
}