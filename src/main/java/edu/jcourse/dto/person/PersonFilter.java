package edu.jcourse.dto.person;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;

@Builder
@FieldNameConstants
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