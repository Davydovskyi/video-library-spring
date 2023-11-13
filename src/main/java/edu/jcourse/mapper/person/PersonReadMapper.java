package edu.jcourse.mapper.person;

import edu.jcourse.database.entity.Person;
import edu.jcourse.dto.movieperson.MoviePersonReadDto;
import edu.jcourse.dto.person.PersonReadDto;
import edu.jcourse.mapper.Mapper;
import edu.jcourse.mapper.movieperson.MoviePersonReadMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonReadMapper implements Mapper<Person, PersonReadDto> {

    private final MoviePersonReadMapper moviePersonReadMapper;

    public PersonReadMapper(@Lazy MoviePersonReadMapper moviePersonReadMapper) {
        this.moviePersonReadMapper = moviePersonReadMapper;
    }

    @Override
    public PersonReadDto map(Person from) {
        return PersonReadDto.builder()
                .id(from.getId())
                .name(from.getName())
                .birthDate(from.getBirthDate())
                .build();
    }

    @Override
    public PersonReadDto fullMap(Person from) {
        List<MoviePersonReadDto> moviePersons = from.getMoviePersons().stream()
                .map(moviePersonReadMapper::fullMap)
                .toList();
        return PersonReadDto.builder()
                .id(from.getId())
                .name(from.getName())
                .birthDate(from.getBirthDate())
                .moviePersons(moviePersons)
                .build();
    }
}