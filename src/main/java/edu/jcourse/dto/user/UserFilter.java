package edu.jcourse.dto.user;

import edu.jcourse.database.entity.User.Fields;
import lombok.Builder;
import lombok.Getter;

@Builder
public record UserFilter(String email,
                         String username,
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