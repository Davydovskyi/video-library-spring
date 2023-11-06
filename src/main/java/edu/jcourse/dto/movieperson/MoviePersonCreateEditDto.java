package edu.jcourse.dto.movieperson;

import edu.jcourse.database.entity.PersonRole;
import lombok.Builder;

@Builder
public record MoviePersonCreateEditDto(
//        @NotNull(message = "{person.error.personId.empty}")
        Integer personId,
//        @NotNull(message = "{person.error.role.empty}")
        PersonRole role) {
}