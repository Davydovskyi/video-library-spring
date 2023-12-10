package edu.jcourse.integration.http.controller;

import edu.jcourse.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static edu.jcourse.dto.movie.MovieCreateEditDto.Fields.*;
import static edu.jcourse.dto.movie.MovieFilter.Fields;
import static edu.jcourse.util.HttpPath.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
@AutoConfigureMockMvc
class MovieControllerIT extends IntegrationTestBase {

    private static final Integer MOVIE_ID = 1;
    private final MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get(MOVIES))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("movie/movies"))
                .andExpect(model().attributeExists("movies", "genres", "filter", "sorts", "metaData"))
                .andExpect(model().attribute("movies", hasSize(3)));
    }

    @Test
    void findAllWithFilter() throws Exception {
        mockMvc.perform(get(MOVIES)
                        .param(Fields.title, "a")
                        .param(Fields.releaseYear, "2008"))
                .andExpect(status().isOk())
                .andExpect(view().name("movie/movies"))
                .andExpect(model().attributeExists("movies", "genres", "filter", "sorts", "metaData"))
                .andExpect(model().attribute("movies", hasSize(1)));
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get(MOVIES + MOVIE_BY_ID, MOVIE_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("movie/movie"))
                .andExpect(model().attributeExists("movie", "reviews", "moviePersons"));
    }

    @Test
    void findByIdWhenNotFound() throws Exception {
        mockMvc.perform(get(MOVIES + MOVIE_BY_ID, 100))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error500"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "404 NOT_FOUND"));
    }

    @Test
    void add() throws Exception {
        mockMvc.perform(get(MOVIES + MOVIE_ADD))
                .andExpect(status().isOk())
                .andExpect(view().name("movie/add-movie"))
                .andExpect(model().attributeExists("movie", "genres"));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post(MOVIES)
                        .param(title, "Title")
                        .param(releaseYear, "2008")
                        .param(country, "USA")
                        .param(genre, "ACTION")
                        .param(description, "some description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(MOVIES));
    }

    @Test
    void createWhenValidationFails() throws Exception {
        mockMvc.perform(post(MOVIES)
                        .param(title, "Title")
                        .param(releaseYear, "2008")
                        .param(country, "")
                        .param(genre, "ACTION")
                        .param(description, "some description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(MOVIES + MOVIE_ADD))
                .andExpect(flash().attributeExists("movie", "errors"))
                .andExpect(flash().attribute("errors", hasSize(1)));
    }

    @Test
    void preUpdate() throws Exception {
        mockMvc.perform(get(MOVIES + MOVIE_PRE_UPDATE, MOVIE_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("movie/update-movie"))
                .andExpect(model().attributeExists("movie", "genres"));
    }

    @Test
    void preUpdateWhenMovieNotFound() throws Exception {
        mockMvc.perform(get(MOVIES + MOVIE_PRE_UPDATE, 100))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error500"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "404 NOT_FOUND"));
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(post(MOVIES + MOVIE_UPDATE, MOVIE_ID)
                        .param(title, "Title")
                        .param(releaseYear, "2008")
                        .param(country, "USA")
                        .param(genre, "ACTION")
                        .param(description, "some description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(MOVIES + "/" + MOVIE_ID));
    }

    @Test
    void updateWhenValidationFails() throws Exception {
        mockMvc.perform(post(MOVIES + MOVIE_UPDATE, MOVIE_ID)
                        .param(title, "Title")
                        .param(releaseYear, "2008")
                        .param(country, "")
                        .param(genre, "ACTION")
                        .param(description, "some description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(MOVIES + "/" + MOVIE_ID + "/pre-update"))
                .andExpect(flash().attributeExists("movie", "errors"))
                .andExpect(flash().attribute("errors", hasSize(1)));
    }

    @Test
    void updateWhenMovieNotFound() throws Exception {
        mockMvc.perform(post(MOVIES + MOVIE_UPDATE, 100)
                        .param(title, "Title")
                        .param(releaseYear, "2008")
                        .param(country, "USA")
                        .param(genre, "ACTION")
                        .param(description, "some description"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error500"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "404 NOT_FOUND"));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(post(MOVIES + MOVIE_DELETE, MOVIE_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(MOVIES));
    }

    @Test
    void deleteWhenMovieNotFound() throws Exception {
        mockMvc.perform(post(MOVIES + MOVIE_DELETE, 100))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error500"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "404 NOT_FOUND"));
    }
}