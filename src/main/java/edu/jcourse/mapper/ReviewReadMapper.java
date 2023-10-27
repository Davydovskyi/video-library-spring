package edu.jcourse.mapper;

import edu.jcourse.database.entity.Review;
import edu.jcourse.dto.ReviewReadDto;
import org.springframework.stereotype.Component;

@Component
public class ReviewReadMapper implements Mapper<Review, ReviewReadDto> {
    @Override
    public ReviewReadDto map(Review from) {
        return null;
    }
}