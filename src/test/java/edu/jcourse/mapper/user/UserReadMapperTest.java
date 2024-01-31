package edu.jcourse.mapper.user;

import edu.jcourse.database.entity.Gender;
import edu.jcourse.database.entity.Role;
import edu.jcourse.database.entity.User;
import edu.jcourse.dto.user.UserReadDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UserReadMapperTest {

    private final UserReadMapper mapper = new UserReadMapper();

    @Test
    void map() {
        User user = buildUser();
        UserReadDto expectedResult = buildUserReadDto();

        UserReadDto actualResult = mapper.map(user);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private User buildUser() {
        return User.builder()
                .id(1L)
                .userName("test")
                .email("test")
                .birthDate(LocalDate.now())
                .role(Role.USER)
                .gender(Gender.MALE)
                .userImage("test")
                .build();
    }

    private UserReadDto buildUserReadDto() {
        return UserReadDto.builder()
                .id(1L)
                .userName("test")
                .email("test")
                .birthDate(LocalDate.now())
                .role(Role.USER)
                .gender(Gender.MALE)
                .image("test")
                .build();
    }
}