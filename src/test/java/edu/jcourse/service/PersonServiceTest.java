package edu.jcourse.service;

import com.querydsl.core.types.Predicate;
import edu.jcourse.database.entity.Person;
import edu.jcourse.database.querydsl.QPredicates;
import edu.jcourse.database.repository.PersonRepository;
import edu.jcourse.dto.person.PersonCreateEditDto;
import edu.jcourse.dto.person.PersonFilter;
import edu.jcourse.dto.person.PersonReadDto;
import edu.jcourse.mapper.person.PersonCreateEditMapper;
import edu.jcourse.mapper.person.PersonReadMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static edu.jcourse.database.entity.Person.Fields;
import static edu.jcourse.database.entity.Person.builder;
import static edu.jcourse.database.entity.QPerson.person;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private PersonReadMapper personReadMapper;
    @Mock
    private PersonCreateEditMapper personCreateEditMapper;
    @Mock
    private Pageable pageable;
    @InjectMocks
    private PersonService personService;

    @Test
    void findAll() {
        List<Person> people = List.of(buildPerson("John"), buildPerson("Jane"));
        doReturn(buildPersonReadDto(1, "John")).when(personReadMapper).map(people.get(0));
        doReturn(buildPersonReadDto(2, "Jane")).when(personReadMapper).map(people.get(1));
        doReturn(people).when(personRepository).findAll(any(Sort.class));
        List<PersonReadDto> expectedResult = List.of(buildPersonReadDto(1, "John"), buildPersonReadDto(2, "Jane"));
        Sort sort = Sort.by(Fields.name).ascending();

        List<PersonReadDto> actualResult = personService.findAll();

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(personRepository).findAll(sort);
        verify(personReadMapper).map(people.get(0));
        verify(personReadMapper).map(people.get(1));
        verifyNoMoreInteractions(personRepository, personReadMapper);
    }

    @Test
    void findAllWhenPersonNotFound() {
        doReturn(List.of()).when(personRepository).findAll(any(Sort.class));

        List<PersonReadDto> actualResult = personService.findAll();

        assertThat(actualResult).isEmpty();
        verify(personRepository).findAll(any(Sort.class));
        verifyNoMoreInteractions(personRepository);
        verifyNoInteractions(personReadMapper);
    }

    @Test
    void findAllWithFilter() {
        List<Person> people = List.of(buildPerson("John"), buildPerson("Jane"));
        doReturn(0).when(pageable).getPageNumber();
        doReturn(20).when(pageable).getPageSize();
        PersonFilter filter = buildPersonFilter("J");
        Predicate predicate = buildPredicateForFindAll();
        PageRequest request = buildPageRequest();
        doReturn(buildPersonReadDto(1, "John")).when(personReadMapper).map(people.get(0));
        doReturn(buildPersonReadDto(2, "Jane")).when(personReadMapper).map(people.get(1));
        doReturn(new PageImpl<>(people)).when(personRepository).findAll(any(Predicate.class), any(Pageable.class));
        Page<PersonReadDto> expectedResult = new PageImpl<>(List.of(buildPersonReadDto(1, "John"), buildPersonReadDto(2, "Jane")));

        Page<PersonReadDto> actualResult = personService.findAll(filter, pageable);

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(personRepository).findAll(predicate, request);
        verify(personReadMapper).map(people.get(0));
        verify(personReadMapper).map(people.get(1));
        verifyNoMoreInteractions(personRepository, personReadMapper);
    }

    @Test
    void findAllWithFilterWhenPersonNotFound() {
        doReturn(0).when(pageable).getPageNumber();
        doReturn(20).when(pageable).getPageSize();
        PersonFilter filter = buildPersonFilter("J");
        Predicate predicate = buildPredicateForFindAll();
        PageRequest request = buildPageRequest();
        doReturn(Page.empty()).when(personRepository).findAll(any(Predicate.class), any(Pageable.class));

        Page<PersonReadDto> actualResult = personService.findAll(filter, pageable);

        assertThat(actualResult).isEmpty();
        verify(personRepository).findAll(predicate, request);
        verifyNoMoreInteractions(personRepository);
        verifyNoInteractions(personReadMapper);
    }

    @Test
    void findById() {
        Person person = buildPerson("John");
        doReturn(Optional.of(person)).when(personRepository).findById(1);
        doReturn(buildPersonReadDto(1, "John")).when(personReadMapper).fullMap(person);
        PersonReadDto expectedResult = buildPersonReadDto(1, "John");

        Optional<PersonReadDto> actualResult = personService.findById(1);

        assertThat(actualResult).contains(expectedResult);
        verify(personRepository).findById(1);
        verify(personReadMapper).fullMap(person);
        verifyNoMoreInteractions(personRepository, personReadMapper);
    }

    @Test
    void findByIdWhenPersonNotFound() {
        doReturn(Optional.empty()).when(personRepository).findById(1);

        Optional<PersonReadDto> actualResult = personService.findById(1);

        assertThat(actualResult).isEmpty();
        verify(personRepository).findById(1);
        verifyNoMoreInteractions(personRepository);
        verifyNoInteractions(personReadMapper);
    }

    @Test
    void create() {
        PersonCreateEditDto createEditDto = buildPersonCreateEditDto();
        Person person = buildPerson("John");
        doReturn(person).when(personCreateEditMapper).map(any());
        doReturn(person).when(personRepository).save(any());
        doReturn(buildPersonReadDto(1, "John")).when(personReadMapper).map(any());
        PersonReadDto expectedResult = buildPersonReadDto(1, "John");

        PersonReadDto actualResult = personService.create(createEditDto);

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(personRepository).save(person);
        verify(personCreateEditMapper).map(createEditDto);
        verify(personReadMapper).map(person);
        verifyNoMoreInteractions(personRepository, personCreateEditMapper, personReadMapper);
    }

    @Test
    void createWhenPersonIsNotSaved() {
        PersonCreateEditDto createEditDto = buildPersonCreateEditDto();
        Person person = buildPerson("John");
        doReturn(person).when(personCreateEditMapper).map(any());
        doReturn(null).when(personRepository).save(any());

        assertThrowsExactly(NoSuchElementException.class, () -> personService.create(createEditDto));
        verify(personRepository).save(person);
        verify(personCreateEditMapper).map(createEditDto);
        verifyNoMoreInteractions(personRepository, personCreateEditMapper);
        verifyNoInteractions(personReadMapper);
    }

    @Test
    void update() {
        PersonCreateEditDto createEditDto = buildPersonCreateEditDto();
        Person person = buildPerson("John");
        doReturn(Optional.of(person)).when(personRepository).findById(any());
        doReturn(person).when(personCreateEditMapper).map(any(), any());
        doReturn(person).when(personRepository).saveAndFlush(any());
        doReturn(buildPersonReadDto(1, "John")).when(personReadMapper).map(any());
        PersonReadDto expectedResult = buildPersonReadDto(1, "John");

        Optional<PersonReadDto> actualResult = personService.update(1, createEditDto);

        assertThat(actualResult).contains(expectedResult);
        verify(personRepository).findById(1);
        verify(personCreateEditMapper).map(createEditDto, person);
        verify(personRepository).saveAndFlush(person);
        verify(personReadMapper).map(person);
        verifyNoMoreInteractions(personRepository, personCreateEditMapper, personReadMapper);
    }

    @Test
    void updateWhenPersonNotFound() {
        PersonCreateEditDto createEditDto = buildPersonCreateEditDto();
        doReturn(Optional.empty()).when(personRepository).findById(any());

        Optional<PersonReadDto> actualResult = personService.update(1, createEditDto);

        assertThat(actualResult).isEmpty();
        verify(personRepository).findById(1);
        verifyNoMoreInteractions(personRepository);
        verifyNoInteractions(personCreateEditMapper, personReadMapper);
    }

    @Test
    void delete() {
        Person person = buildPerson("John");
        doReturn(Optional.of(person)).when(personRepository).findById(any());

        boolean actualResult = personService.delete(1);

        assertThat(actualResult).isTrue();
        verify(personRepository).findById(1);
        verify(personRepository).delete(person);
        verify(personRepository).flush();
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void deleteWhenPersonNotFound() {
        doReturn(Optional.empty()).when(personRepository).findById(any());

        boolean actualResult = personService.delete(1);

        assertThat(actualResult).isFalse();
        verify(personRepository).findById(1);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void findByAllFields() {
        PersonFilter filter = buildPersonFilter("John");
        Person person = buildPerson("John");
        Predicate predicate = buildPredicateForFindByAllFields();
        doReturn(Optional.of(person)).when(personRepository).findOne(any(Predicate.class));
        doReturn(buildPersonReadDto(1, "John")).when(personReadMapper).map(any());
        PersonReadDto expectedResult = buildPersonReadDto(1, "John");

        Optional<PersonReadDto> actualResult = personService.findByAllFields(filter);

        assertThat(actualResult).contains(expectedResult);
        verify(personRepository).findOne(predicate);
        verify(personReadMapper).map(person);
        verifyNoMoreInteractions(personRepository, personReadMapper);
    }

    @Test
    void findByAllFieldsWhenPersonNotFound() {
        PersonFilter filter = buildPersonFilter("John");
        Predicate predicate = buildPredicateForFindByAllFields();
        doReturn(Optional.empty()).when(personRepository).findOne(any(Predicate.class));

        Optional<PersonReadDto> actualResult = personService.findByAllFields(filter);

        assertThat(actualResult).isEmpty();
        verify(personRepository).findOne(predicate);
        verifyNoMoreInteractions(personRepository);
        verifyNoInteractions(personReadMapper);
    }

    private PersonFilter buildPersonFilter(String name) {
        return PersonFilter.builder()
                .name(name)
                .birthYear((short) 2019)
                .birthDate(LocalDate.now())
                .build();
    }

    private Predicate buildPredicateForFindAll() {
        return QPredicates.builder()
                .add("J", person.name::containsIgnoreCase)
                .add(2019, person.birthDate.year()::in)
                .buildAnd();
    }

    private Predicate buildPredicateForFindByAllFields() {
        return QPredicates.builder()
                .add("John", person.name::equalsIgnoreCase)
                .add(LocalDate.now(), person.birthDate::eq)
                .buildAnd();
    }

    private PersonReadDto buildPersonReadDto(Integer id, String name) {
        return PersonReadDto.builder()
                .id(id)
                .name(name)
                .birthDate(LocalDate.now())
                .build();
    }

    private Person buildPerson(String name) {
        return builder()
                .name(name)
                .birthDate(LocalDate.now())
                .build();
    }

    private PersonCreateEditDto buildPersonCreateEditDto() {
        return PersonCreateEditDto.builder()
                .name("John")
                .birthDate(LocalDate.now())
                .build();
    }

    private PageRequest buildPageRequest() {
        return PageRequest.of(0, 20, Sort.by(Fields.name).ascending());
    }
}