package edu.jcourse.mapper;

import edu.jcourse.database.entity.User;
import edu.jcourse.dto.ReviewReadDto;
import edu.jcourse.dto.UserReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    private final ReviewReadMapper reviewReadMapper;

    @Override
    public UserReadDto map(User from) {
        return UserReadDto.builder()
                .id(from.getId())
                .username(from.getUserName())
                .email(from.getEmail())
                .birthDate(from.getBirthDate())
                .role(from.getRole())
                .gender(from.getGender())
                .image(from.getUserImage())
                .build();
    }

    @Override
    public UserReadDto fullMap(User from) {
        List<ReviewReadDto> reviews = from.getReviews().stream()
                .map(reviewReadMapper::map)
                .toList();

        return UserReadDto.builder()
                .id(from.getId())
                .username(from.getUserName())
                .email(from.getEmail())
                .birthDate(from.getBirthDate())
                .role(from.getRole())
                .gender(from.getGender())
                .image(from.getUserImage())
                .reviews(reviews)
                .build();
    }
}