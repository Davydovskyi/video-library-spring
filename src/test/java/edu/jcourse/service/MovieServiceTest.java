package edu.jcourse.service;

import com.querydsl.core.types.Predicate;
import edu.jcourse.database.entity.Genre;
import edu.jcourse.database.entity.Movie;
import edu.jcourse.database.querydsl.QPredicates;
import edu.jcourse.database.repository.MovieRepository;
import edu.jcourse.dto.movie.MovieCreateEditDto;
import edu.jcourse.dto.movie.MovieFilter;
import edu.jcourse.dto.movie.MovieReadDto;
import edu.jcourse.mapper.movie.MovieCreateEditMapper;
import edu.jcourse.mapper.movie.MovieReadMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static edu.jcourse.database.entity.QMovie.movie;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private MovieReadMapper movieReadMapper;
    @Mock
    private MovieCreateEditMapper movieCreateEditMapper;
    @Mock
    private Pageable pageable;
    @InjectMocks
    private MovieService movieService;

    @Test
    void findAll() {
        MovieFilter movieFilter = buildMovieFilter();
        Predicate predicate = buildPredicateForFindAll();
        doReturn(1).when(pageable).getPageNumber();
        doReturn(20).when(pageable).getPageSize();
        PageRequest pageRequest = buildPageRequest();
        List<Movie> movies = List.of(buildMovie("title"), buildMovie("title2"));
        doReturn(new PageImpl<>(movies)).when(movieRepository).findAll(any(Predicate.class), any(PageRequest.class));
        doReturn(buildMovieReadDto("title")).when(movieReadMapper).map(movies.get(0));
        doReturn(buildMovieReadDto("title2")).when(movieReadMapper).map(movies.get(1));
        PageImpl<MovieReadDto> expectedResult = new PageImpl<>(List.of(buildMovieReadDto("title"), buildMovieReadDto("title2")));

        Page<MovieReadDto> actualResult = movieService.findAll(movieFilter, pageable);

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(pageable).getPageNumber();
        verify(pageable).getPageSize();
        verify(movieRepository).findAll(predicate, pageRequest);
        verify(movieReadMapper).map(movies.get(0));
        verify(movieReadMapper).map(movies.get(1));
        verifyNoMoreInteractions(movieReadMapper);
    }

    @Test
    void findAllWhenMovieNotFound() {
        MovieFilter movieFilter = buildMovieFilter();
        doReturn(1).when(pageable).getPageNumber();
        doReturn(20).when(pageable).getPageSize();
        Predicate predicate = buildPredicateForFindAll();
        PageRequest pageRequest = buildPageRequest();
        doReturn(Page.empty()).when(movieRepository).findAll(any(Predicate.class), any(PageRequest.class));

        Page<MovieReadDto> actualResult = movieService.findAll(movieFilter, pageable);

        assertThat(actualResult).isEmpty();
        verify(pageable).getPageNumber();
        verify(pageable).getPageSize();
        verify(movieRepository).findAll(predicate, pageRequest);
        verifyNoInteractions(movieReadMapper);
    }

    @Test
    void findById() {
        Movie movie = buildMovie("title");
        doReturn(Optional.of(movie)).when(movieRepository).findById(1);
        doReturn(buildMovieReadDto("title")).when(movieReadMapper).fullMap(movie);

        Optional<MovieReadDto> actualResult = movieService.findById(1);

        assertThat(actualResult).contains(buildMovieReadDto("title"));
        verify(movieRepository).findById(1);
        verify(movieReadMapper).fullMap(movie);
        verifyNoMoreInteractions(movieReadMapper);
    }

    @Test
    void findByIdWhenMovieNotFound() {
        doReturn(Optional.empty()).when(movieRepository).findById(1);

        Optional<MovieReadDto> actualResult = movieService.findById(1);

        assertThat(actualResult).isEmpty();
        verify(movieRepository).findById(1);
        verifyNoInteractions(movieReadMapper);
    }

    @Test
    void create() {
        Movie movie = buildMovie("title");
        MovieCreateEditDto movieCreateEditDto = buildMovieCreateEditDto();
        doReturn(movie).when(movieCreateEditMapper).map(any());
        doReturn(movie).when(movieRepository).save(any());
        doReturn(buildMovieReadDto("title")).when(movieReadMapper).map(movie);

        MovieReadDto actualResult = movieService.create(movieCreateEditDto);

        assertThat(actualResult).isEqualTo(buildMovieReadDto("title"));
        verify(movieCreateEditMapper).map(movieCreateEditDto);
        verify(movieRepository).save(movie);
        verify(movieReadMapper).map(movie);
        verifyNoMoreInteractions(movieReadMapper);
    }

    @Test
    void createWhenMovieIsNotSaved() {
        Movie movie = buildMovie("title");
        MovieCreateEditDto movieCreateEditDto = buildMovieCreateEditDto();
        doReturn(movie).when(movieCreateEditMapper).map(any());
        doReturn(null).when(movieRepository).save(any());

        assertThrowsExactly(NoSuchElementException.class, () -> movieService.create(movieCreateEditDto));
        verify(movieCreateEditMapper).map(movieCreateEditDto);
        verify(movieRepository).save(movie);
        verifyNoInteractions(movieReadMapper);
    }

    @Test
    void findByAllFields() {
        Movie movie = buildMovie("title");
        MovieFilter movieFilter = buildMovieFilter();
        Predicate predicate = buildPredicateForFindByAllFields();
        doReturn(Optional.of(movie)).when(movieRepository).findOne(any(Predicate.class));
        doReturn(buildMovieReadDto("title")).when(movieReadMapper).map(any());

        Optional<MovieReadDto> actualResult = movieService.findByAllFields(movieFilter);

        assertThat(actualResult).contains(buildMovieReadDto("title"));
        verify(movieRepository).findOne(predicate);
        verify(movieReadMapper).map(movie);
        verifyNoMoreInteractions(movieReadMapper);
    }

    @Test
    void findByAllFieldsWhenMovieNotFound() {
        MovieFilter movieFilter = buildMovieFilter();
        Predicate predicate = buildPredicateForFindByAllFields();
        doReturn(Optional.empty()).when(movieRepository).findOne(any(Predicate.class));

        Optional<MovieReadDto> actualResult = movieService.findByAllFields(movieFilter);

        assertThat(actualResult).isEmpty();
        verify(movieRepository).findOne(predicate);
        verifyNoInteractions(movieReadMapper);
    }

    @Test
    void update() {
        Movie movie = buildMovie("title");
        MovieCreateEditDto movieCreateEditDto = buildMovieCreateEditDto();
        MovieReadDto movieReadDto = buildMovieReadDto("title");
        doReturn(movieReadDto).when(movieReadMapper).map(any());
        doReturn(movie).when(movieCreateEditMapper).map(any(), any());
        doReturn(Optional.of(movie)).when(movieRepository).findById(any());
        doReturn(movie).when(movieRepository).saveAndFlush(any());

        Optional<MovieReadDto> actualResult = movieService.update(1, movieCreateEditDto);

        assertThat(actualResult).contains(buildMovieReadDto("title"));
        verify(movieRepository).findById(1);
        verify(movieRepository).saveAndFlush(movie);
        verify(movieCreateEditMapper).map(movieCreateEditDto, movie);
        verify(movieReadMapper).map(movie);
        verifyNoMoreInteractions(movieReadMapper, movieRepository);
    }

    @Test
    void updateWhenMovieNotFound() {
        MovieCreateEditDto movieCreateEditDto = buildMovieCreateEditDto();
        doReturn(Optional.empty()).when(movieRepository).findById(any());

        Optional<MovieReadDto> actualResult = movieService.update(1, movieCreateEditDto);

        assertThat(actualResult).isEmpty();
        verify(movieRepository).findById(1);
        verifyNoMoreInteractions(movieRepository);
        verifyNoInteractions(movieCreateEditMapper, movieReadMapper);
    }

    @Test
    void delete() {
        Movie movie = buildMovie("title");
        doReturn(Optional.of(movie)).when(movieRepository).findById(any());

        boolean actualResult = movieService.delete(1);

        assertThat(actualResult).isTrue();
        verify(movieRepository).findById(1);
        verify(movieRepository).delete(movie);
        verify(movieRepository).flush();
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void deleteWhenMovieNotFound() {
        doReturn(Optional.empty()).when(movieRepository).findById(any());

        boolean actualResult = movieService.delete(1);

        assertThat(actualResult).isFalse();
        verify(movieRepository).findById(1);
        verifyNoMoreInteractions(movieRepository);
    }

    private MovieCreateEditDto buildMovieCreateEditDto() {
        return MovieCreateEditDto.builder()
                .title("title")
                .releaseYear((short) 2022)
                .country("country")
                .genre(Genre.ACTION)
                .description("description")
                .build();
    }

    private MovieFilter buildMovieFilter() {
        return MovieFilter.builder()
                .title("title")
                .releaseYear((short) 2022)
                .country("country")
                .genre(Genre.ACTION)
                .build();
    }

    private PageRequest buildPageRequest() {
        return PageRequest.of(1, 20, Sort.by(Movie.Fields.title).ascending());
    }

    private Predicate buildPredicateForFindAll() {
        return QPredicates.builder()
                .add("title", movie.title::containsIgnoreCase)
                .add((short) 2022, movie.releaseYear::eq)
                .add("country", movie.country::containsIgnoreCase)
                .add(Genre.ACTION, movie.genre::eq)
                .buildAnd();
    }

    private Predicate buildPredicateForFindByAllFields() {
        return QPredicates.builder()
                .add("title", movie.title::equalsIgnoreCase)
                .add((short) 2022, movie.releaseYear::eq)
                .add("country", movie.country::equalsIgnoreCase)
                .add(Genre.ACTION, movie.genre::eq)
                .buildAnd();
    }

    private Movie buildMovie(String title) {
        return Movie.builder()
                .title(title)
                .releaseYear((short) 2022)
                .country("country")
                .genre(Genre.ACTION)
                .description("description")
                .build();
    }

    private MovieReadDto buildMovieReadDto(String title) {
        return MovieReadDto.builder()
                .title(title)
                .releaseYear((short) 2022)
                .country("country")
                .genre(Genre.ACTION)
                .description("description")
                .build();
    }
}