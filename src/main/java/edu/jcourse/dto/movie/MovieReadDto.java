package edu.jcourse.dto.movie;

import edu.jcourse.database.entity.Genre;
import edu.jcourse.dto.movieperson.MoviePersonReadDto;
import edu.jcourse.dto.review.ReviewReadDto;
import lombok.Builder;

import java.util.List;

@Builder
public record MovieReadDto(Integer id,
                           String title,
                           Short releaseYear,
                           String country,
                           Genre genre,
                           String description,
                           List<ReviewReadDto> reviews,
                           List<MoviePersonReadDto> moviePersons) {
}