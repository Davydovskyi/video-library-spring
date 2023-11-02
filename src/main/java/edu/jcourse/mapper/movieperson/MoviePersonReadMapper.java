package edu.jcourse.mapper.movieperson;

import edu.jcourse.database.entity.MoviePerson;
import edu.jcourse.dto.movieperson.MoviePersonReadDto;
import edu.jcourse.dto.person.PersonReadDto;
import edu.jcourse.mapper.Mapper;
import edu.jcourse.mapper.person.PersonReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MoviePersonReadMapper implements Mapper<MoviePerson, MoviePersonReadDto> {
    private final PersonReadMapper personReadMapper;

    @Override
    public MoviePersonReadDto map(MoviePerson from) {
        PersonReadDto person = Optional.ofNullable(from.getPerson())
                .map(personReadMapper::map)
                .orElse(null);

        return MoviePersonReadDto.builder()
                .id(from.getId())
                .role(from.getPersonRole())
                .person(person)
                .build();
    }
}