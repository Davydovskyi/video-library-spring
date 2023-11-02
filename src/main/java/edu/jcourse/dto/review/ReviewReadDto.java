package edu.jcourse.dto.review;

import edu.jcourse.dto.movie.MovieReadDto;
import edu.jcourse.dto.user.UserReadDto;
import lombok.Builder;

@Builder
public record ReviewReadDto(Integer id,
                            MovieReadDto movie,
                            UserReadDto user,
                            String reviewText,
                            Short rate) {
}