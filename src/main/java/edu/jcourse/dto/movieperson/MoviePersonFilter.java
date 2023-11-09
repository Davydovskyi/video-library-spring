package edu.jcourse.dto.movieperson;

import edu.jcourse.database.entity.PersonRole;
import lombok.Builder;

@Builder
public record MoviePersonFilter(Integer personId,
                                Integer movieId,
                                PersonRole role) {
}