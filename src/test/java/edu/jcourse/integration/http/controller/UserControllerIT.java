package edu.jcourse.integration.http.controller;

import edu.jcourse.database.entity.Gender;
import edu.jcourse.database.entity.Role;
import edu.jcourse.dto.user.UserCreateEditDto;
import edu.jcourse.dto.user.UserFilter.Fields;
import edu.jcourse.dto.user.UserReadDto;
import edu.jcourse.integration.IntegrationTestBase;
import edu.jcourse.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static edu.jcourse.dto.user.UserCreateEditDto.Fields.*;
import static edu.jcourse.util.HttpPath.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
@AutoConfigureMockMvc
class UserControllerIT extends IntegrationTestBase {

    private static final Long USER_ID = 1L;
    private final MockMvc mockMvc;
    private final UserService userService;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get(USERS))
                .andExpect(status().isOk())
                .andExpect(view().name("user/users"))
                .andExpect(model().attributeExists("users", "metaData", "filter", "sorts"))
                .andExpect(model().attribute("users", hasSize(3)));
    }

    @Test
    void findAllByFilter() throws Exception {
        mockMvc.perform(get(USERS)
                        .param(Fields.email, "gmail"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/users"))
                .andExpect(model().attributeExists("users", "metaData", "filter", "sorts"))
                .andExpect(model().attribute("users", hasSize(3)));
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get(USERS + USER_BY_ID, USER_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user"))
                .andExpect(model().attributeExists("user", "roles", "genders"));
    }

    @Test
    void findByIdWhenNotFound() throws Exception {
        mockMvc.perform(get(USERS + USER_BY_ID, 100))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error500"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "404 NOT_FOUND"));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(multipart(USERS)
                        .file(new MockMultipartFile("image", "image.png", "image/png", new byte[0]))
                        .param(email, "abc@gmail.com")
                        .param(rawPassword, "12345678")
                        .param(userName, "username")
                        .param(birthDate, "1990-09-10")
                        .param(role, "USER")
                        .param(gender, "MALE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LOGIN));
    }

    @Test
    void createWhenValidationFailed() throws Exception {
        mockMvc.perform(multipart(USERS)
                        .file(new MockMultipartFile("image", "image.png", "image/png", new byte[0]))
                        .param(email, "abc@gmail.com")
                        .param(rawPassword, "123456")
                        .param(userName, "")
                        .param(birthDate, "1990-09-10")
                        .param(role, "USER")
                        .param(gender, "MALE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(REGISTRATION))
                .andExpect(flash().attributeExists("errors", "user"))
                .andExpect(flash().attribute("errors", hasSize(2)));
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(multipart(USERS + USER_UPDATE, USER_ID)
                        .file(new MockMultipartFile("image", "image.png", "image/png", new byte[0]))
                        .param(email, "abc@gmail.com")
                        .param(rawPassword, "12345678")
                        .param(userName, "username")
                        .param(birthDate, "1990-09-10")
                        .param(role, "USER")
                        .param(gender, "MALE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(USERS + "/" + USER_ID));
    }

    @Test
    void updateWhenValidationFailed() throws Exception {
        mockMvc.perform(multipart(USERS + USER_UPDATE, USER_ID)
                        .file(new MockMultipartFile("image", "image.png", "image/png", new byte[0]))
                        .param(email, "abc@gmail.com")
                        .param(rawPassword, "123456")
                        .param(userName, "")
                        .param(birthDate, "1990-09-10")
                        .param(role, "USER")
                        .param(gender, "MALE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(USERS + "/" + USER_ID))
                .andExpect(flash().attributeExists("errors", "user"))
                .andExpect(flash().attribute("errors", hasSize(2)));
    }

    @Test
    void updateWhenNotFound() throws Exception {
        mockMvc.perform(multipart(USERS + USER_UPDATE, 100)
                        .file(new MockMultipartFile("image", "image.png", "image/png", new byte[0]))
                        .param(email, "abc@gmail.com")
                        .param(rawPassword, "12345678")
                        .param(userName, "username")
                        .param(birthDate, "1990-09-10")
                        .param(role, "USER")
                        .param(gender, "MALE"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error500"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "404 NOT_FOUND"));
    }

    @Test
    void delete() throws Exception {
        UserReadDto userReadDto = userService.create(UserCreateEditDto.builder()
                .email("abc@gmail.com")
                .userName("username")
                .role(Role.USER)
                .rawPassword("12345678")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1990, 9, 10))
                .image(new MockMultipartFile("image.png", new byte[0]))
                .build());
        mockMvc.perform(post(USERS + USER_DELETE, userReadDto.id()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(USERS));
    }

    @Test
    void deleteWhenNotFound() throws Exception {
        mockMvc.perform(post(USERS + USER_DELETE, 100))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error500"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "404 NOT_FOUND"));
    }
}