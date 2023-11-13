package edu.jcourse.dto.movie;

import edu.jcourse.database.entity.Genre;
import edu.jcourse.database.entity.Movie.Fields;
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