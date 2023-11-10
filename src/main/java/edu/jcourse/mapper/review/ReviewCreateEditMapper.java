package edu.jcourse.mapper.review;

import edu.jcourse.database.entity.Movie;
import edu.jcourse.database.entity.Review;
import edu.jcourse.database.entity.User;
import edu.jcourse.dto.review.ReviewCreateEditDto;
import edu.jcourse.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class ReviewCreateEditMapper implements Mapper<ReviewCreateEditDto, Review> {
    @Override
    public Review map(ReviewCreateEditDto from) {
        return Review.builder()
                .user(User.builder()
                        .id(from.userId())
                        .build())
                .movie(Movie.builder()
                        .id(from.movieId())
                        .build())
                .reviewText(from.reviewText())
                .rate(from.rate())
                .build();
    }
}