package edu.jcourse.dto.person;

import edu.jcourse.validation.PersonInfo;
import edu.jcourse.validation.group.CreateAction;
import edu.jcourse.validation.group.UpdateAction;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@PersonInfo(groups = {CreateAction.class, UpdateAction.class})
public record PersonCreateEditDto(
        @NotEmpty(message = "{person.error.name.empty}")
        String name,
        @NotNull(message = "{person.error.birthDate.empty}")
        @Past(message = "{person.error.birthDate.invalid}")
        LocalDate birthDate) {
}