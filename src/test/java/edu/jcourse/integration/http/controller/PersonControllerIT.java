package edu.jcourse.integration.http.controller;

import edu.jcourse.dto.person.PersonCreateEditDto.Fields;
import edu.jcourse.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static edu.jcourse.dto.person.PersonFilter.Fields.birthYear;
import static edu.jcourse.dto.person.PersonFilter.Fields.name;
import static edu.jcourse.util.HttpPath.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
@AutoConfigureMockMvc
class PersonControllerIT extends IntegrationTestBase {

    private static final Integer PERSON_ID = 1;
    private final MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get(PERSONS))
                .andExpect(status().isOk())
                .andExpect(view().name("person/persons"))
                .andExpect(model().attributeExists("persons", "metaData", "filter", "sorts"))
                .andExpect(model().attribute("persons", hasSize(3)));
    }

    @Test
    void findAllByFilter() throws Exception {
        mockMvc.perform(get(PERSONS)
                        .param(name, "a")
                        .param(birthYear, "1990"))
                .andExpect(status().isOk())
                .andExpect(view().name("person/persons"))
                .andExpect(model().attributeExists("persons", "metaData", "filter", "sorts"))
                .andExpect(model().attribute("persons", hasSize(1)));
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get(PERSONS + PERSON_BY_ID, PERSON_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("person/person"))
                .andExpect(model().attributeExists("person", "movies"));
    }

    @Test
    void findByIdWhenNotFound() throws Exception {
        mockMvc.perform(get(PERSONS + PERSON_BY_ID, 100))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error500"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "404 NOT_FOUND"));
    }

    @Test
    void add() throws Exception {
        mockMvc.perform(get(PERSONS + PERSON_ADD))
                .andExpect(status().isOk())
                .andExpect(view().name("person/add-person"))
                .andExpect(model().attributeExists("person"));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post(PERSONS)
                        .param(Fields.name, "Name")
                        .param(Fields.birthDate, "1990-01-01"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(PERSONS));
    }

    @Test
    void createWhenValidationFailed() throws Exception {
        mockMvc.perform(post(PERSONS)
                        .param(Fields.name, "")
                        .param(Fields.birthDate, "1990-01-01"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(PERSONS + PERSON_ADD))
                .andExpect(flash().attributeExists("person", "errors"))
                .andExpect(flash().attribute("errors", hasSize(1)));
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(post(PERSONS + PERSON_UPDATE, PERSON_ID)
                        .param(Fields.name, "Name")
                        .param(Fields.birthDate, "1990-01-01"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/persons/" + PERSON_ID));
    }

    @Test
    void updateWhenValidationFailed() throws Exception {
        mockMvc.perform(post(PERSONS + PERSON_UPDATE, PERSON_ID)
                        .param(Fields.name, "")
                        .param(Fields.birthDate, "1990-01-01"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/persons/" + PERSON_ID))
                .andExpect(flash().attributeExists("person", "errors"))
                .andExpect(flash().attribute("errors", hasSize(1)));
    }

    @Test
    void updateWhenNotFound() throws Exception {
        mockMvc.perform(post(PERSONS + PERSON_UPDATE, 100)
                        .param(Fields.name, "Name")
                        .param(Fields.birthDate, "1990-01-01"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error500"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "404 NOT_FOUND"));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(post(PERSONS + PERSON_DELETE, PERSON_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(PERSONS));
    }

    @Test
    void deleteWhenNotFound() throws Exception {
        mockMvc.perform(post(PERSONS + PERSON_DELETE, 100))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error500"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "404 NOT_FOUND"));
    }
}