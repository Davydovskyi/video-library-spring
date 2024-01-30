package edu.jcourse.dto.person;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.jcourse.dto.movieperson.MoviePersonReadDto;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PersonReadDto(Integer id,
                            String name,
                            LocalDate birthDate,
                            List<MoviePersonReadDto> moviePersons) {
}