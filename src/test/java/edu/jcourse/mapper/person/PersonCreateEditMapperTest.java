package edu.jcourse.mapper.person;

import edu.jcourse.database.entity.Person;
import edu.jcourse.dto.person.PersonCreateEditDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PersonCreateEditMapperTest {

    private final PersonCreateEditMapper mapper = new PersonCreateEditMapper();

    @Test
    void mapForCreate() {
        PersonCreateEditDto from = buildPersonCreateEditDto("name");
        Person expectedResult = buildPerson("name");

        Person actualResult = mapper.map(from);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void mapForEdit() {
        PersonCreateEditDto from = buildPersonCreateEditDto("name");
        Person expectedResult = buildPerson("name");
        Person to = buildPerson("test");

        Person actualResult = mapper.map(from, to);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private PersonCreateEditDto buildPersonCreateEditDto(String name) {
        return PersonCreateEditDto.builder()
                .name(name)
                .birthDate(LocalDate.now())
                .build();
    }

    private Person buildPerson(String name) {
        return Person.builder()
                .name(name)
                .birthDate(LocalDate.now())
                .build();
    }
}