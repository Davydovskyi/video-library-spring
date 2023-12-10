package edu.jcourse.dto.movie;

import edu.jcourse.database.entity.Genre;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record MovieFilter(String title,
                          Genre genre,
                          Short releaseYear,
                          String country,
                          Sort sortBy) {
    @Getter
    public enum Sort {
        TITLE(Fields.title),
        GENRE(Fields.genre),
        RELEASE_YEAR(Fields.releaseYear),
        COUNTRY(Fields.country);

        private final String name;

        Sort(String name) {
            this.name = name;
        }
    }
}