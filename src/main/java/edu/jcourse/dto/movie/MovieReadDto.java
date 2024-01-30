package edu.jcourse.dto.movie;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.jcourse.database.entity.Genre;
import edu.jcourse.dto.movieperson.MoviePersonReadDto;
import edu.jcourse.dto.review.ReviewReadDto;
import lombok.Builder;

import java.util.List;
import java.util.Objects;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MovieReadDto(Integer id,
                           String title,
                           Short releaseYear,
                           String country,
                           Genre genre,
                           String description,
                           List<ReviewReadDto> reviews,
                           List<MoviePersonReadDto> moviePersons) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieReadDto that = (MovieReadDto) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}