package edu.jcourse.integration.service;

import edu.jcourse.database.entity.Gender;
import edu.jcourse.database.entity.Role;
import edu.jcourse.dto.user.AdaptedUserDetails;
import edu.jcourse.dto.user.UserCreateEditDto;
import edu.jcourse.dto.user.UserFilter;
import edu.jcourse.dto.user.UserReadDto;
import edu.jcourse.integration.IntegrationTestBase;
import edu.jcourse.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@RequiredArgsConstructor
class UserServiceIT extends IntegrationTestBase {

    private static final Long USER_ID = 1L;
    private static final String USER_EMAIL = "user1@gmail.com";
    private final UserService userService;

    static Stream<Arguments> getFindAllUserArguments() {
        return Stream.of(
                Arguments.of(buildUserFilter(null, null), buildPageable(0, 10), 3),
                Arguments.of(buildUserFilter(null, null), buildPageable(0, 2), 2),
                Arguments.of(buildUserFilter(null, null), buildPageable(1, 10), 0),
                Arguments.of(buildUserFilter("a", null), buildPageable(0, 10), 3),
                Arguments.of(buildUserFilter("a", "a"), buildPageable(0, 10), 1),
                Arguments.of(buildUserFilter("a", "b"), buildPageable(0, 10), 0)
        );
    }

    private static UserFilter buildUserFilter(String email, String username) {
        return UserFilter.builder()
                .email(email)
                .userName(username)
                .build();
    }

    private static Pageable buildPageable(int pageNumber, int pageSize) {
        return PageRequest.of(pageNumber, pageSize);
    }

    @Test
    void create() {
        UserCreateEditDto user = buildUserCreateEditDto("username");

        UserReadDto actualResult = userService.create(user);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.id()).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("getFindAllUserArguments")
    void findAllByFilter(UserFilter filter, Pageable pageable, int expectedSize) {
        assertThat(userService.findAll(filter, pageable)).hasSize(expectedSize);
    }

    @Test
    void findById() {
        assertThat(userService.findById(USER_ID)).isPresent();
    }

    @Test
    void findByIdWhenNotFound() {
        assertThat(userService.findById(100L)).isEmpty();
    }

    @Test
    void findByEmail() {
        assertThat(userService.findByEmail(USER_EMAIL)).isPresent();
    }

    @Test
    void findByEmailWhenNotFound() {
        assertThat(userService.findByEmail("unknown@mail")).isEmpty();
    }

    @Test
    void update() {
        UserCreateEditDto user = buildUserCreateEditDto("username");
        UserCreateEditDto editDto = buildUserCreateEditDto("new username");
        UserReadDto userReadDto = userService.create(user);

        Optional<UserReadDto> actualResult = userService.update(userReadDto.id(), editDto);

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().userName()).isEqualTo("new username");
    }

    @Test
    void updateWhenNotFound() {
        UserCreateEditDto editDto = buildUserCreateEditDto("new username");

        Optional<UserReadDto> actualResult = userService.update(100L, editDto);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void delete() {
        UserCreateEditDto userCreateEditDto = buildUserCreateEditDto("username");
        UserReadDto userReadDto = userService.create(userCreateEditDto);

        assertThat(userService.delete(userReadDto.id())).isTrue();
    }

    @Test
    void deleteWhenNotFound() {
        assertThat(userService.delete(100L)).isFalse();
    }

    @Test
    void loadUserByUsername() {
        AdaptedUserDetails userDetails = (AdaptedUserDetails) userService.loadUserByUsername(USER_EMAIL);

        assertThat(userDetails.getId()).isEqualTo(USER_ID);
        assertThat(userDetails.getUsername()).isEqualTo(USER_EMAIL);
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo(Role.USER.name());
        assertThat(userDetails.getPassword()).isNotNull();
    }

    @Test
    void loadUserByUsernameWhenNotFound() {
        assertThrowsExactly(UsernameNotFoundException.class, () -> userService.loadUserByUsername("unknown@mail"));
    }

    private UserCreateEditDto buildUserCreateEditDto(String username) {
        return UserCreateEditDto.builder()
                .userName(username)
                .email("email")
                .rawPassword("password")
                .birthDate(LocalDate.now())
                .role(Role.USER)
                .gender(Gender.MALE)
                .image(new MockMultipartFile("image", new byte[0]))
                .build();
    }
}