package edu.jcourse.integration.http.controller;

import edu.jcourse.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static edu.jcourse.util.HttpPath.REGISTRATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
@AutoConfigureMockMvc
class RegistrationControllerIT extends IntegrationTestBase {

    private final MockMvc mockMvc;

    @Test
    void registration() throws Exception {
        mockMvc.perform(get(REGISTRATION))
                .andExpect(status().isOk())
                .andExpect(view().name("user/registration"))
                .andExpect(model().attributeExists("user", "genders", "userRole"));
    }
}