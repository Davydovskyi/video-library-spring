package edu.jcourse.dto.person;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;

@Builder
@FieldNameConstants
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PersonFilter(String name,
                           Short birthYear,
                           LocalDate birthDate,
                           Sort sortBy) {

    @Getter
    public enum Sort {
        NAME(Fields.name),
        BIRTH_YEAR(Fields.birthDate);

        private final String name;

        Sort(String name) {
            this.name = name;
        }
    }
}