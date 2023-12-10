package edu.jcourse.integration.config;

import edu.jcourse.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.stream.Stream;

import static edu.jcourse.util.HttpPath.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@AutoConfigureMockMvc
class SecurityConfigurationIT extends IntegrationTestBase {

    private final MockMvc mockMvc;

    static Stream<Arguments> getOnlyAdminHttpRequest() {
        return Stream.of(
                Arguments.of(get(USERS + USER_DELETE, 1)),
                Arguments.of(get(USERS + USER_UPDATE, 1)),
                Arguments.of(get(MOVIE_PERSONS + MOVIE_PERSONS_ADD, 1)),
                Arguments.of(get(MOVIES + MOVIE_ADD)),
                Arguments.of(post(MOVIES)),
                Arguments.of(get(MOVIES + MOVIE_PRE_UPDATE, 1)),
                Arguments.of(get(MOVIES + MOVIE_DELETE, 1)),
                Arguments.of(get(MOVIES + MOVIE_UPDATE, 1)),
                Arguments.of(get(PERSONS + PERSON_ADD)),
                Arguments.of(post(PERSONS)),
                Arguments.of(get(PERSONS + PERSON_DELETE, 1)),
                Arguments.of(get(PERSONS + PERSON_UPDATE, 1))
        );
    }

    static Stream<Arguments> getNoAuthorityHttpRequest() {
        return Stream.of(
                Arguments.of(get(LOGIN)),
                Arguments.of(get(REGISTRATION)),
                Arguments.of(get("/v3/api-docs/swagger-config")),
                Arguments.of(get("/swagger-ui/index.html"))
        );
    }

    @ParameterizedTest
    @MethodSource("getOnlyAdminHttpRequest")
    @WithMockUser(username = "test@gmail.com", password = "test", authorities = {"USER"})
    void testAdminAccess(MockHttpServletRequestBuilder request) throws Exception {
        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @MethodSource("getNoAuthorityHttpRequest")
    @WithMockUser(username = "test@gmail.com", password = "test")
    void testNoAuthority(MockHttpServletRequestBuilder request) throws Exception {
        mockMvc.perform(request)
                .andExpect(status().isOk());
    }
}