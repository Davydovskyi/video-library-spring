package edu.jcourse.dto.movieperson;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.jcourse.database.entity.PersonRole;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MoviePersonFilter(Integer personId,
                                Integer movieId,
                                PersonRole role) {
}