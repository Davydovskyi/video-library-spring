package edu.jcourse.mapper.review;

import edu.jcourse.database.entity.Review;
import edu.jcourse.dto.movie.MovieReadDto;
import edu.jcourse.dto.review.ReviewReadDto;
import edu.jcourse.dto.user.UserReadDto;
import edu.jcourse.mapper.Mapper;
import edu.jcourse.mapper.movie.MovieReadMapper;
import edu.jcourse.mapper.user.UserReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewReadMapper implements Mapper<Review, ReviewReadDto> {

    private final UserReadMapper userReadMapper;
    private final MovieReadMapper movieReadMapper;

    @Override
    public ReviewReadDto map(Review from) {
        UserReadDto user = Optional.ofNullable(from.getUser())
                .map(userReadMapper::map)
                .orElse(null);

        MovieReadDto movie = Optional.ofNullable(from.getMovie())
                .map(movieReadMapper::map)
                .orElse(null);

        return ReviewReadDto.builder()
                .id(from.getId())
                .reviewText(from.getReviewText())
                .rate(from.getRate())
                .user(user)
                .movie(movie)
                .build();
    }
}