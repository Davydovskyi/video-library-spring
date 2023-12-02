package edu.jcourse.dto.review;

import lombok.Builder;

@Builder
public record ReviewFilter(Long userId,
                           Integer movieId) {
}