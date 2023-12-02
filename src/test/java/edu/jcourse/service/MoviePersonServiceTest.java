package edu.jcourse.service;

import com.querydsl.core.types.Predicate;
import edu.jcourse.database.entity.Movie;
import edu.jcourse.database.entity.MoviePerson;
import edu.jcourse.database.entity.Person;
import edu.jcourse.database.entity.PersonRole;
import edu.jcourse.database.querydsl.QPredicates;
import edu.jcourse.database.repository.MoviePersonRepository;
import edu.jcourse.dto.movie.MovieReadDto;
import edu.jcourse.dto.movieperson.MoviePersonCreateEditDto;
import edu.jcourse.dto.movieperson.MoviePersonFilter;
import edu.jcourse.dto.movieperson.MoviePersonReadDto;
import edu.jcourse.dto.person.PersonReadDto;
import edu.jcourse.mapper.movieperson.MoviePersonCreateEditMapper;
import edu.jcourse.mapper.movieperson.MoviePersonReadMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static edu.jcourse.database.entity.QMoviePerson.moviePerson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoviePersonServiceTest {

    @Mock
    private MoviePersonRepository moviePersonRepository;
    @Mock
    private MoviePersonCreateEditMapper moviePersonCreateEditMapper;
    @Mock
    private MoviePersonReadMapper moviePersonReadMapper;
    @InjectMocks
    private MoviePersonService moviePersonService;

    @Test
    void create() {
        MoviePersonCreateEditDto moviePersonCreateEditDto = buildMoviePersonCreateEditDto();
        MoviePerson moviePerson = buildMoviePerson();
        MoviePersonReadDto moviePersonReadDto = buildMoviePersonReadDto();
        doReturn(moviePerson).when(moviePersonCreateEditMapper).map(any());
        doReturn(moviePerson).when(moviePersonRepository).save(any());
        doReturn(moviePersonReadDto).when(moviePersonReadMapper).map(any());
        MoviePersonReadDto expectedResult = buildMoviePersonReadDto();

        MoviePersonReadDto actualResult = moviePersonService.create(moviePersonCreateEditDto);
        assertThat(actualResult).isEqualTo(expectedResult);
        verify(moviePersonCreateEditMapper).map(moviePersonCreateEditDto);
        verify(moviePersonRepository).save(moviePerson);
        verify(moviePersonReadMapper).map(moviePerson);
    }

    @Test
    void createWhenMoviePersonIsNotSaved() {
        MoviePersonCreateEditDto moviePersonCreateEditDto = buildMoviePersonCreateEditDto();
        MoviePerson moviePerson = buildMoviePerson();
        doReturn(moviePerson).when(moviePersonCreateEditMapper).map(any());
        doReturn(null).when(moviePersonRepository).save(any());

        assertThrowsExactly(NoSuchElementException.class, () -> moviePersonService.create(moviePersonCreateEditDto));
        verify(moviePersonCreateEditMapper).map(moviePersonCreateEditDto);
        verify(moviePersonRepository).save(moviePerson);
        verifyNoInteractions(moviePersonReadMapper);
    }

    @Test
    void findByAllFields() {
        MoviePersonFilter moviePersonFilter = buildMoviePersonFilter();
        MoviePersonReadDto moviePersonReadDto = buildMoviePersonReadDto();
        MoviePerson moviePerson = buildMoviePerson();
        Predicate predicate = buildPredicate();
        doReturn(Optional.of(moviePerson)).when(moviePersonRepository).findOne(any(Predicate.class));
        doReturn(moviePersonReadDto).when(moviePersonReadMapper).map(any());
        MoviePersonReadDto expectedResult = buildMoviePersonReadDto();

        Optional<MoviePersonReadDto> actualResult = moviePersonService.findByAllFields(moviePersonFilter);
        assertThat(actualResult).isPresent().contains(expectedResult);
        verify(moviePersonRepository).findOne(predicate);
        verify(moviePersonReadMapper).map(moviePerson);
    }

    @Test
    void findByAllFieldsWhenMoviePersonNotFound() {
        MoviePersonFilter moviePersonFilter = buildMoviePersonFilter();
        Predicate predicate = buildPredicate();
        doReturn(Optional.empty()).when(moviePersonRepository).findOne(any(Predicate.class));

        Optional<MoviePersonReadDto> actualResult = moviePersonService.findByAllFields(moviePersonFilter);
        assertThat(actualResult).isEmpty();
        verify(moviePersonRepository).findOne(predicate);
        verifyNoInteractions(moviePersonReadMapper);
    }

    private MoviePersonCreateEditDto buildMoviePersonCreateEditDto() {
        return MoviePersonCreateEditDto.builder()
                .personId(1)
                .movieId(1)
                .role(PersonRole.ACTOR)
                .build();
    }

    private MoviePerson buildMoviePerson() {
        return MoviePerson.builder()
                .person(Person.builder()
                        .id(1)
                        .build())
                .movie(Movie.builder()
                        .id(1)
                        .build())
                .personRole(PersonRole.ACTOR)
                .build();
    }

    private MoviePersonReadDto buildMoviePersonReadDto() {
        return MoviePersonReadDto.builder()
                .person(PersonReadDto.builder()
                        .id(1)
                        .build())
                .movie(MovieReadDto.builder()
                        .id(1)
                        .build())
                .role(PersonRole.ACTOR)
                .build();
    }

    private MoviePersonFilter buildMoviePersonFilter() {
        return MoviePersonFilter.builder()
                .personId(1)
                .movieId(1)
                .role(PersonRole.ACTOR)
                .build();
    }

    private Predicate buildPredicate() {
        return QPredicates.builder()
                .add(1, moviePerson.person.id::eq)
                .add(1, moviePerson.movie.id::eq)
                .add(PersonRole.ACTOR, moviePerson.personRole::eq)
                .buildAnd();
    }
}