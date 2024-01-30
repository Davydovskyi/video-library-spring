package edu.jcourse.dto.movieperson;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.jcourse.database.entity.PersonRole;
import edu.jcourse.dto.movie.MovieReadDto;
import edu.jcourse.dto.person.PersonReadDto;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MoviePersonReadDto(Long id,
                                 PersonReadDto person,
                                 MovieReadDto movie,
                                 PersonRole role) {
}