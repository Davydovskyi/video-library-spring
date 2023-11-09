package edu.jcourse.mapper.movieperson;

import edu.jcourse.database.entity.MoviePerson;
import edu.jcourse.dto.movie.MovieReadDto;
import edu.jcourse.dto.movieperson.MoviePersonReadDto;
import edu.jcourse.dto.person.PersonReadDto;
import edu.jcourse.mapper.Mapper;
import edu.jcourse.mapper.movie.MovieReadMapper;
import edu.jcourse.mapper.person.PersonReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MoviePersonReadMapper implements Mapper<MoviePerson, MoviePersonReadDto> {
    private final PersonReadMapper personReadMapper;
    private final MovieReadMapper movieReadMapper;

    @Override
    public MoviePersonReadDto fullMap(MoviePerson from) {
        PersonReadDto person = Optional.ofNullable(from.getPerson())
                .map(personReadMapper::map)
                .orElse(null);
        MovieReadDto movie = Optional.ofNullable(from.getMovie())
                .map(movieReadMapper::map)
                .orElse(null);

        return MoviePersonReadDto.builder()
                .id(from.getId())
                .role(from.getPersonRole())
                .person(person)
                .movie(movie)
                .build();
    }

    @Override
    public MoviePersonReadDto map(MoviePerson from) {
        return MoviePersonReadDto.builder()
                .id(from.getId())
                .build();
    }
}