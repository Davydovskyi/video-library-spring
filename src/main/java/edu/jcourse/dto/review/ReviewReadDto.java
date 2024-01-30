package edu.jcourse.dto.review;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.jcourse.dto.movie.MovieReadDto;
import edu.jcourse.dto.user.UserReadDto;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ReviewReadDto(Integer id,
                            MovieReadDto movie,
                            UserReadDto user,
                            String reviewText,
                            Short rate) {
}