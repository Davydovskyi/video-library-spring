package edu.jcourse.dto.movieperson;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.jcourse.database.entity.PersonRole;
import edu.jcourse.validation.MoviePersonInfo;
import edu.jcourse.validation.group.CreateAction;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@MoviePersonInfo(groups = {CreateAction.class})
@FieldNameConstants
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MoviePersonCreateEditDto(
        @NotNull(message = "{person.error.personId.empty}")
        Integer personId,
        @NotNull(message = "{person.error.movieId.empty}")
        Integer movieId,
        @NotNull(message = "{person.error.role.empty}")
        PersonRole role) {
}