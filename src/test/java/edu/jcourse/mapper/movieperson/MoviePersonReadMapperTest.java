package edu.jcourse.mapper.movieperson;

import edu.jcourse.database.entity.Movie;
import edu.jcourse.database.entity.MoviePerson;
import edu.jcourse.database.entity.Person;
import edu.jcourse.database.entity.PersonRole;
import edu.jcourse.dto.movie.MovieReadDto;
import edu.jcourse.dto.movieperson.MoviePersonReadDto;
import edu.jcourse.dto.person.PersonReadDto;
import edu.jcourse.mapper.movie.MovieReadMapper;
import edu.jcourse.mapper.person.PersonReadMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoviePersonReadMapperTest {

    @Mock
    private PersonReadMapper personReadMapper;
    @Mock
    private MovieReadMapper movieReadMapper;
    @InjectMocks
    private MoviePersonReadMapper moviePersonReadMapper;

    @Test
    void map() {
        MoviePerson moviePerson = buildMoviePerson();
        MoviePersonReadDto expectedResult = buildMoviePersonReadDto(null, null);

        MoviePersonReadDto actualResult = moviePersonReadMapper.map(moviePerson);

        assertThat(actualResult).isEqualTo(expectedResult);
        verifyNoInteractions(personReadMapper, movieReadMapper);
    }

    @Test
    void fullMap() {
        MoviePerson moviePerson = buildMoviePerson();
        doReturn(PersonReadDto.builder().build()).when(personReadMapper).map(any());
        doReturn(MovieReadDto.builder().build()).when(movieReadMapper).map(any());
        MoviePersonReadDto expectedResult = buildMoviePersonReadDto(PersonReadDto.builder().build(), MovieReadDto.builder().build());

        MoviePersonReadDto actualResult = moviePersonReadMapper.fullMap(moviePerson);

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(personReadMapper).map(any());
        verify(movieReadMapper).map(any());
    }

    private MoviePerson buildMoviePerson() {
        return MoviePerson.builder()
                .id(1L)
                .personRole(PersonRole.ACTOR)
                .movie(new Movie())
                .person(new Person())
                .build();
    }

    private MoviePersonReadDto buildMoviePersonReadDto(PersonReadDto personReadDto, MovieReadDto movieReadDto) {
        return MoviePersonReadDto.builder()
                .id(1L)
                .role(PersonRole.ACTOR)
                .person(personReadDto)
                .movie(movieReadDto)
                .build();
    }
}