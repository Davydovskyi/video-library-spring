package edu.jcourse.dto.review;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.jcourse.validation.ReviewInfo;
import edu.jcourse.validation.group.CreateAction;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@ReviewInfo(groups = {CreateAction.class})
@FieldNameConstants
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ReviewCreateEditDto(
        @NotNull(message = "{review.error.userId.empty}")
        Long userId,
        @NotNull(message = "{review.error.movieId.empty}")
        Integer movieId,
        @NotBlank(message = "{review.error.reviewText.empty}")
        String reviewText,
        @NotNull(message = "{review.error.rate.empty}")
        @Min(value = 1, message = "{review.error.rate.invalid}")
        @Max(value = 10, message = "{review.error.rate.invalid}")
        Short rate) {
}