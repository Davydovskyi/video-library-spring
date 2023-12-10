package edu.jcourse.integration.http.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.jcourse.database.entity.Gender;
import edu.jcourse.database.entity.Role;
import edu.jcourse.dto.user.UserCreateEditDto;
import edu.jcourse.dto.user.UserReadDto;
import edu.jcourse.integration.IntegrationTestBase;
import edu.jcourse.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static edu.jcourse.util.HttpPath.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
@AutoConfigureMockMvc
class UserRestControllerIT extends IntegrationTestBase {

    private static final Long USER_ID = 1L;
    private static final Long USER_ID_WITH_IMAGE = 3L;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get(REST_USERS))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content", hasSize(3))
                );
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get(REST_USERS + USER_BY_ID, USER_ID))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(USER_ID)
                );
    }

    @Test
    void findByIdWhenNotFound() throws Exception {
        mockMvc.perform(get(REST_USERS + USER_BY_ID, 100))
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        UserCreateEditDto userCreateEditDto = buildUserCreateEditDto("username");
        mockMvc.perform(post(REST_USERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateEditDto)))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").isNumber(),
                        jsonPath("$.id", not(equalTo(0))),
                        jsonPath("$.username").value("username")
                );
    }

    @Test
    void createWhenValidationFailed() throws Exception {
        UserCreateEditDto userCreateEditDto = buildUserCreateEditDto("");
        mockMvc.perform(post(REST_USERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateEditDto)))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message").value("Validation error"),
                        jsonPath("$.errors").value(hasKey("username"))
                );
    }

    @Test
    void update() throws Exception {
        UserCreateEditDto userCreateEditDto = buildUserCreateEditDto("username");
        mockMvc.perform(put(REST_USERS + USER_BY_ID, USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateEditDto)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(USER_ID),
                        jsonPath("$.username").value("username"),
                        jsonPath("$.role").value(Role.USER.toString()),
                        jsonPath("$.gender").value(Gender.MALE.toString()),
                        jsonPath("$.birthDate").value(LocalDate.of(2000, 1, 1).toString()),
                        jsonPath("$.email").value("email@email.com")
                );
    }

    @Test
    void updateWhenValidationFailed() throws Exception {
        UserCreateEditDto userCreateEditDto = buildUserCreateEditDto("");
        mockMvc.perform(put(REST_USERS + USER_BY_ID, USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateEditDto)))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message").value("Validation error"),
                        jsonPath("$.errors").value(hasKey("username"))
                );
    }

    @Test
    void updateWhenNotFound() throws Exception {
        UserCreateEditDto userCreateEditDto = buildUserCreateEditDto("username");
        mockMvc.perform(put(REST_USERS + USER_BY_ID, 100)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateEditDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete() throws Exception {
        UserCreateEditDto userCreateEditDto = buildUserCreateEditDto("username");
        UserReadDto userReadDto = userService.create(userCreateEditDto);
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_USERS + USER_BY_ID, userReadDto.id()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteWhenNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_USERS + USER_BY_ID, 100))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAvatar() throws Exception {
        mockMvc.perform(get(REST_USERS + USER_AVATAR, USER_ID_WITH_IMAGE))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_OCTET_STREAM)
                );
    }

    @Test
    void getAvatarWhenUserNotFound() throws Exception {
        mockMvc.perform(get(REST_USERS + USER_AVATAR, 100))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAvatarWhenImageNotFound() throws Exception {
        mockMvc.perform(get(REST_USERS + USER_AVATAR, USER_ID))
                .andExpect(status().isNotFound());
    }

    private UserCreateEditDto buildUserCreateEditDto(String username) {
        return UserCreateEditDto.builder()
                .email("email@email.com")
                .username(username)
                .rawPassword("password")
                .role(Role.USER)
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();
    }
}