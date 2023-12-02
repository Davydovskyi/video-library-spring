package edu.jcourse.mapper.person;

import edu.jcourse.database.entity.MoviePerson;
import edu.jcourse.database.entity.Person;
import edu.jcourse.dto.movieperson.MoviePersonReadDto;
import edu.jcourse.dto.person.PersonReadDto;
import edu.jcourse.mapper.movieperson.MoviePersonReadMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonReadMapperTest {

    @Mock
    private MoviePersonReadMapper moviePersonReadMapper;
    @InjectMocks
    private PersonReadMapper personReadMapper;

    @Test
    void map() {
        Person person = buildPerson();
        PersonReadDto expectedResult = buildPersonReadDto(null);

        PersonReadDto actualResult = personReadMapper.map(person);

        assertThat(actualResult).isEqualTo(expectedResult);
        verifyNoInteractions(moviePersonReadMapper);
    }

    @Test
    void fullMap() {
        Person person = buildPerson();
        doReturn(MoviePersonReadDto.builder().build()).when(moviePersonReadMapper).fullMap(any());
        PersonReadDto expectedResult = buildPersonReadDto(List.of(MoviePersonReadDto.builder().build()));

        PersonReadDto actualResult = personReadMapper.fullMap(person);

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(moviePersonReadMapper).fullMap(any());
    }

    private Person buildPerson() {
        return Person.builder()
                .id(1)
                .name("name")
                .birthDate(LocalDate.now())
                .moviePersons(List.of(new MoviePerson()))
                .build();
    }

    private PersonReadDto buildPersonReadDto(List<MoviePersonReadDto> moviePersons) {
        return PersonReadDto.builder()
                .id(1)
                .name("name")
                .birthDate(LocalDate.now())
                .moviePersons(moviePersons)
                .build();
    }
}