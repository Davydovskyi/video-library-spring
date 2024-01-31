package edu.jcourse.integration.http.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.jcourse.database.entity.Genre;
import edu.jcourse.dto.movie.MovieCreateEditDto;
import edu.jcourse.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static edu.jcourse.util.HttpPath.MOVIE_BY_ID;
import static edu.jcourse.util.HttpPath.REST_MOVIES;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@AutoConfigureMockMvc
class MovieRestControllerIT extends IntegrationTestBase {

    private static final Integer MOVIE_ID = 1;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get(REST_MOVIES))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content", hasSize(3))
                );
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get(REST_MOVIES + MOVIE_BY_ID, MOVIE_ID))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(MOVIE_ID),
                        jsonPath("$.movie_persons", hasSize(2))
                );
    }

    @Test
    void findByIdWhenNotFound() throws Exception {
        mockMvc.perform(get(REST_MOVIES + MOVIE_BY_ID, 100))
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        MovieCreateEditDto createEditDto = buildMovieCreateEditDto("Title");
        mockMvc.perform(post(REST_MOVIES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEditDto)))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").isNumber(),
                        jsonPath("$.id", not(equalTo(0))),
                        jsonPath("$.title").value("Title")
                );
    }

    @Test
    void createWhenValidationFailed() throws Exception {
        MovieCreateEditDto createEditDto = buildMovieCreateEditDto("");
        mockMvc.perform(post(REST_MOVIES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEditDto)))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message").value("Validation error"),
                        jsonPath("$.errors").value(hasKey("title"))
                );
    }

    @Test
    void update() throws Exception {
        MovieCreateEditDto createEditDto = buildMovieCreateEditDto("Title");
        mockMvc.perform(put(REST_MOVIES + MOVIE_BY_ID, MOVIE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEditDto)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(MOVIE_ID),
                        jsonPath("$.title").value("Title"),
                        jsonPath("$.country").value("Country"),
                        jsonPath("$.genre").value("ACTION"),
                        jsonPath("$.release_year").value(2022),
                        jsonPath("$.description").value("Description")
                );
    }

    @Test
    void updateWhenNotFound() throws Exception {
        MovieCreateEditDto createEditDto = buildMovieCreateEditDto("Title");
        mockMvc.perform(put(REST_MOVIES + MOVIE_BY_ID, 100)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEditDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateWhenValidationFailed() throws Exception {
        MovieCreateEditDto createEditDto = buildMovieCreateEditDto("");
        mockMvc.perform(put(REST_MOVIES + MOVIE_BY_ID, MOVIE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEditDto)))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message").value("Validation error"),
                        jsonPath("$.errors").value(hasKey("title"))
                );
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_MOVIES + MOVIE_BY_ID, MOVIE_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteWhenNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_MOVIES + MOVIE_BY_ID, 100))
                .andExpect(status().isNotFound());
    }

    private MovieCreateEditDto buildMovieCreateEditDto(String title) {
        return MovieCreateEditDto.builder()
                .title(title)
                .country("Country")
                .genre(Genre.ACTION)
                .releaseYear((short) 2022)
                .description("Description")
                .build();
    }
}

// if throws exception
//mockMvc.perform(
//        get("/persons/1"))
//        .andExpect(status().isNotFound())
//        .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException .class));

// if we want to check whole json
//mockMvc.perform(
//        delete("/persons/{id}", person.getId()))
//        .andExpect(status().isOk())
//        .andExpect(content().json(objectMapper.writeValueAsString(person)));
