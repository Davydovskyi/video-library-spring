package edu.jcourse.dto.movie;

import edu.jcourse.database.entity.Genre;
import lombok.Builder;
import lombok.Getter;

@Builder
public record MovieFilter(String title,
                          Genre genre,
                          Short releaseYear,
                          String country,
                          Sort sortBy) {
    @Getter
    public enum Sort {
        TITLE("title"),
        GENRE("genre"),
        RELEASE_YEAR("releaseYear"),
        COUNTRY("country");

        private final String name;

        Sort(String name) {
            this.name = name;
        }
    }
}