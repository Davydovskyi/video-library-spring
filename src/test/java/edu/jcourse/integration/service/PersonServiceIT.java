package edu.jcourse.integration.service;

import edu.jcourse.dto.person.PersonCreateEditDto;
import edu.jcourse.dto.person.PersonFilter;
import edu.jcourse.dto.person.PersonReadDto;
import edu.jcourse.integration.IntegrationTestBase;
import edu.jcourse.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class PersonServiceIT extends IntegrationTestBase {

    private static final Integer PERSON_ID = 1;
    private final PersonService personService;

    static Stream<Arguments> getFindAllPersonArguments() {
        return Stream.of(
                Arguments.of(buildPersonFilter(null, null, null), buildPageable(0, 10), 3),
                Arguments.of(buildPersonFilter(null, null, null), buildPageable(1, 10), 0),
                Arguments.of(buildPersonFilter(null, null, null), buildPageable(0, 2), 2),
                Arguments.of(buildPersonFilter("a", null, null), buildPageable(0, 10), 3),
                Arguments.of(buildPersonFilter("Emma", null, null), buildPageable(0, 10), 1),
                Arguments.of(buildPersonFilter("Emma", (short) 1990, null), buildPageable(0, 10), 1),
                Arguments.of(buildPersonFilter(null, (short) 1963, null), buildPageable(0, 10), 1),
                Arguments.of(buildPersonFilter("Radcliffe", (short) 1963, null), buildPageable(0, 10), 0)
        );
    }

    private static PersonFilter buildPersonFilter(String name, Short birthYear, LocalDate birthDate) {
        return PersonFilter.builder()
                .name(name)
                .birthDate(birthDate)
                .birthYear(birthYear)
                .build();
    }

    private static Pageable buildPageable(int page, int size) {
        return PageRequest.of(page, size);
    }

    @Test
    void findAll() {
        assertThat(personService.findAll()).hasSize(3).isSortedAccordingTo(Comparator.comparing(PersonReadDto::name));
    }

    @ParameterizedTest
    @MethodSource("getFindAllPersonArguments")
    void findAllByFilter(PersonFilter filter, Pageable pageable, int expectedSize) {
        assertThat(personService.findAll(filter, pageable)).hasSize(expectedSize);
    }

    @Test
    void findById() {
        assertThat(personService.findById(PERSON_ID)).isPresent();
    }

    @Test
    void findByIdWhenNotFound() {
        assertThat(personService.findById(100)).isEmpty();
    }

    @Test
    void create() {
        PersonCreateEditDto person = buildPersonCreateEditDto("Name");

        PersonReadDto actualResult = personService.create(person);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.id()).isNotNull();
    }

    @Test
    void findByAllFields() {
        assertThat(personService.findByAllFields(buildPersonFilter("Emma Watson", null, LocalDate.of(1990, 4, 25)))).isPresent();
    }

    @Test
    void findByAllFieldsWhenNotFound() {
        assertThat(personService.findByAllFields(buildPersonFilter("Emma Watson", null, LocalDate.of(1990, 3, 25)))).isNotPresent();
    }

    @Test
    void update() {
        PersonCreateEditDto person = buildPersonCreateEditDto("Name");
        PersonCreateEditDto editDto = buildPersonCreateEditDto("new Name");
        PersonReadDto actualResult = personService.create(person);

        Optional<PersonReadDto> updated = personService.update(actualResult.id(), editDto);

        assertThat(updated).isPresent();
        assertThat(updated.get().name()).isEqualTo("new Name");
    }

    @Test
    void updateWhenNotFound() {
        PersonCreateEditDto editDto = buildPersonCreateEditDto("new Name");

        Optional<PersonReadDto> updated = personService.update(100, editDto);

        assertThat(updated).isEmpty();
    }

    @Test
    void delete() {
        assertThat(personService.delete(PERSON_ID)).isTrue();
    }

    @Test
    void deleteWhenNotFound() {
        assertThat(personService.delete(100)).isFalse();
    }

    private PersonCreateEditDto buildPersonCreateEditDto(String name) {
        return PersonCreateEditDto.builder()
                .name(name)
                .birthDate(LocalDate.now())
                .build();
    }

}