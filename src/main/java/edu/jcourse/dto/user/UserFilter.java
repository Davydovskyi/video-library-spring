package edu.jcourse.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import static edu.jcourse.database.entity.User.Fields.userName;

@Builder
@FieldNameConstants
public record UserFilter(String email,
                         String username,
                         Sort sortBy) {
    @Getter
    public enum Sort {
        USERNAME(userName),
        EMAIL(Fields.email);

        private final String name;

        Sort(String name) {
            this.name = name;
        }
    }
}