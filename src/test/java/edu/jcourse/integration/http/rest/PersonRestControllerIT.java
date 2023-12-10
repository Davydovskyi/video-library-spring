package edu.jcourse.integration.http.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.jcourse.dto.person.PersonCreateEditDto;
import edu.jcourse.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static edu.jcourse.util.HttpPath.PERSON_BY_ID;
import static edu.jcourse.util.HttpPath.REST_PERSONS;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@AutoConfigureMockMvc
class PersonRestControllerIT extends IntegrationTestBase {

    private static final Integer PERSON_ID = 1;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get(REST_PERSONS))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content", hasSize(3))
                );
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get(REST_PERSONS + PERSON_BY_ID, PERSON_ID))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(PERSON_ID)
                );
    }

    @Test
    void findByIdWhenNotFound() throws Exception {
        mockMvc.perform(get(REST_PERSONS + PERSON_BY_ID, 100))
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        PersonCreateEditDto createEditDto = buildPersonCreateEditDto("Name");
        mockMvc.perform(post(REST_PERSONS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEditDto)))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").isNumber(),
                        jsonPath("$.id", not(equalTo(0))),
                        jsonPath("$.name").value("Name")
                );
    }

    @Test
    void createWhenValidationFailed() throws Exception {
        PersonCreateEditDto createEditDto = buildPersonCreateEditDto("");
        mockMvc.perform(post(REST_PERSONS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEditDto)))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message").value("Validation error"),
                        jsonPath("$.errors").value(hasKey("name"))
                );
    }

    @Test
    void update() throws Exception {
        PersonCreateEditDto createEditDto = buildPersonCreateEditDto("Name");
        mockMvc.perform(put(REST_PERSONS + PERSON_BY_ID, PERSON_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEditDto)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(PERSON_ID),
                        jsonPath("$.birthDate").value(LocalDate.of(2000, 1, 1).toString()),
                        jsonPath("$.name").value("Name")
                );
    }

    @Test
    void updateWhenValidationFailed() throws Exception {
        PersonCreateEditDto createEditDto = buildPersonCreateEditDto("");
        mockMvc.perform(put(REST_PERSONS + PERSON_BY_ID, PERSON_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEditDto)))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message").value("Validation error"),
                        jsonPath("$.errors").value(hasKey("name"))
                );
    }

    @Test
    void updateWhenNotFound() throws Exception {
        PersonCreateEditDto createEditDto = buildPersonCreateEditDto("Name");
        mockMvc.perform(put(REST_PERSONS + PERSON_BY_ID, 100)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEditDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_PERSONS + PERSON_BY_ID, PERSON_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteWhenNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_PERSONS + PERSON_BY_ID, 100))
                .andExpect(status().isNotFound());
    }

    private PersonCreateEditDto buildPersonCreateEditDto(String name) {
        return PersonCreateEditDto
                .builder()
                .name(name)
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();
    }
}