package edu.jcourse.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.jcourse.database.entity.Gender;
import edu.jcourse.database.entity.Role;
import edu.jcourse.dto.review.ReviewReadDto;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserReadDto(Long id,
                          String userName,
                          String email,
                          LocalDate birthDate,
                          Role role,
                          Gender gender,
                          String image,
                          List<ReviewReadDto> reviews) {
}