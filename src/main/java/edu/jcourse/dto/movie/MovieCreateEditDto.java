package edu.jcourse.dto.movie;

import edu.jcourse.database.entity.Genre;
import edu.jcourse.validation.MovieInfo;
import edu.jcourse.validation.group.CreateAction;
import edu.jcourse.validation.group.UpdateAction;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@MovieInfo(groups = {CreateAction.class, UpdateAction.class})
@FieldNameConstants
public record MovieCreateEditDto(
        @NotEmpty(message = "{movie.error.title.empty}")
        String title,
        @NotNull(message = "{movie.error.releaseYear.empty}")
        @Min(value = 1900, message = "{movie.error.releaseYear.invalid}")
        Short releaseYear,
        @NotEmpty(message = "{movie.error.country.empty}")
        String country,
        @NotNull(message = "{movie.error.genre.empty}")
        Genre genre,
        @NotEmpty(message = "{movie.error.description.empty}")
        String description) {
}