package edu.jcourse.dto;

import edu.jcourse.database.entity.Gender;
import edu.jcourse.database.entity.Role;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record UserReadDto(Long id,
                          String username,
                          String email,
                          LocalDate birthDate,
                          Role role,
                          Gender gender,
                          String image,
                          List<ReviewReadDto> reviews) {
}