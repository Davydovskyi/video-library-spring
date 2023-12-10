package edu.jcourse.integration.http.controller;

import edu.jcourse.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static edu.jcourse.util.HttpPath.LOGIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RequiredArgsConstructor
@AutoConfigureMockMvc
class LoginControllerIT extends IntegrationTestBase {

    private final MockMvc mockMvc;

    @Test
    void login() throws Exception {
        mockMvc.perform(get(LOGIN))
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"));
    }
}