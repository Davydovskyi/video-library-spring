package edu.jcourse.dto.user;

import lombok.Builder;
import lombok.Getter;

@Builder
public record UserFilter(String email,
                         String username,
                         Sort sortBy) {
    @Getter
    public enum Sort {
        USERNAME("userName"), EMAIL("email");

        private final String name;

        Sort(String name) {
            this.name = name;
        }
    }
}