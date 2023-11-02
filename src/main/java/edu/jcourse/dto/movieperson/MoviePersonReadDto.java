package edu.jcourse.dto.movieperson;

import edu.jcourse.database.entity.PersonRole;
import edu.jcourse.dto.movie.MovieReadDto;
import edu.jcourse.dto.person.PersonReadDto;
import lombok.Builder;

@Builder
public record MoviePersonReadDto(Long id,
                                 PersonReadDto person,
                                 MovieReadDto movie,
                                 PersonRole role) {
}