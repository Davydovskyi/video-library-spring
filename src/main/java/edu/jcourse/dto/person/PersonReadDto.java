package edu.jcourse.dto.person;

import edu.jcourse.dto.movieperson.MoviePersonReadDto;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PersonReadDto(Integer id,
                            String name,
                            LocalDate birthDate,
                            List<MoviePersonReadDto> moviePersons) {
}