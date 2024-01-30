package edu.jcourse.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserFilter(String email,
                         String userName,
                         Sort sortBy) {
    @Getter
    public enum Sort {
        USERNAME(Fields.userName),
        EMAIL(Fields.email);

        private final String name;

        Sort(String name) {
            this.name = name;
        }
    }
}