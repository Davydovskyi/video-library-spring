package edu.jcourse.integration.http.controller;

import edu.jcourse.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static edu.jcourse.dto.review.ReviewCreateEditDto.Fields.*;
import static edu.jcourse.util.HttpPath.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
@AutoConfigureMockMvc
class ReviewControllerIT extends IntegrationTestBase {

    private static final Long USER_ID = 1L;
    private static final Integer MOVIE_ID = 1;
    private final MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get(REVIEWS + REVIEWS_BY_USER_ID, USER_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("showReviews", "reviews"))
                .andExpect(flash().attribute("reviews", hasSize(2)))
                .andExpect(redirectedUrl(USERS + "/" + USER_ID));
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com", userDetailsServiceBeanName = "userService")
    void add() throws Exception {
        mockMvc.perform(get(REVIEWS + REVIEW_ADD, MOVIE_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("showAddReview", "review", "userId"))
                .andExpect(redirectedUrl(MOVIES + "/" + MOVIE_ID));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post(REVIEWS)
                        .param(movieId, "1")
                        .param(userId, "2")
                        .param(reviewText, "text")
                        .param(rate, "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(MOVIES + "/" + MOVIE_ID));
    }

    @Test
    void createWhenValidationFailed() throws Exception {
        mockMvc.perform(post(REVIEWS)
                        .param(movieId, "1")
                        .param(userId, "1")
                        .param(reviewText, "")
                        .param(rate, "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(REVIEWS + "/add/" + MOVIE_ID))
                .andExpect(flash().attributeExists("review", "errors"))
                .andExpect(flash().attribute("errors", hasSize(1)));
    }
}