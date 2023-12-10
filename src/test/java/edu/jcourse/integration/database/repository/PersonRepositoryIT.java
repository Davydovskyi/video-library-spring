package edu.jcourse.integration.database.repository;

import edu.jcourse.database.entity.Person;
import edu.jcourse.database.repository.PersonRepository;
import edu.jcourse.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class PersonRepositoryIT extends IntegrationTestBase {

    private static final Integer EMMA_WATSON_ID = 1;
    private final PersonRepository personRepository;

    @Test
    void findById() {
        Optional<Person> person = personRepository.findById(EMMA_WATSON_ID);

        assertThat(person).isPresent();
        assertThat(person.get().getMoviePersons()).hasSize(2);
        assertThat(person.get().getMoviePersons().get(0).getMovie()).isNotNull();
        assertThat(person.get().getMoviePersons().get(1).getMovie()).isNotNull();
    }

    @Test
    void findByIdWhenNotFound() {
        Optional<Person> person = personRepository.findById(999);

        assertThat(person).isEmpty();
    }
}