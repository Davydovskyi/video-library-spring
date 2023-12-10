package edu.jcourse.integration.http.controller;

import edu.jcourse.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static edu.jcourse.dto.movieperson.MoviePersonCreateEditDto.Fields.*;
import static edu.jcourse.util.HttpPath.MOVIE_PERSONS;
import static edu.jcourse.util.HttpPath.MOVIE_PERSONS_ADD;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
@AutoConfigureMockMvc
class MoviePersonControllerIT extends IntegrationTestBase {

    private static final Integer MOVIE_ID = 1;
    private final MockMvc mockMvc;

    @Test
    void add() throws Exception {
        mockMvc.perform(get(MOVIE_PERSONS + MOVIE_PERSONS_ADD, MOVIE_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movies/" + MOVIE_ID))
                .andExpect(flash().attributeExists("moviePerson", "showAddParticipant", "persons", "movieRoles"));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post(MOVIE_PERSONS)
                        .param(personId, "1")
                        .param(movieId, "1")
                        .param(role, "DIRECTOR"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movies/" + MOVIE_ID));
    }

    @Test
    void createWhenValidationFails() throws Exception {
        mockMvc.perform(post(MOVIE_PERSONS)
                        .param(personId, "1")
                        .param(movieId, "1")
                        .param(role, (String) null))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movie-persons/add/" + MOVIE_ID))
                .andExpect(flash().attributeExists("moviePerson", "errors"))
                .andExpect(flash().attribute("errors", hasSize(1)));
    }
}