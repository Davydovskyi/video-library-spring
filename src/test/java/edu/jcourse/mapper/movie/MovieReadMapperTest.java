package edu.jcourse.mapper.movie;

import edu.jcourse.database.entity.Genre;
import edu.jcourse.database.entity.Movie;
import edu.jcourse.database.entity.MoviePerson;
import edu.jcourse.database.entity.Review;
import edu.jcourse.dto.movie.MovieReadDto;
import edu.jcourse.dto.movieperson.MoviePersonReadDto;
import edu.jcourse.dto.review.ReviewReadDto;
import edu.jcourse.mapper.movieperson.MoviePersonReadMapper;
import edu.jcourse.mapper.review.ReviewReadMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieReadMapperTest {

    @Mock
    private MoviePersonReadMapper moviePersonReadMapper;
    @Mock
    private ReviewReadMapper reviewReadMapper;
    @InjectMocks
    private MovieReadMapper movieReadMapper;

    @Test
    void map() {
        MovieReadDto actualResult = movieReadMapper.map(buildMovie());

        MovieReadDto expectedResult = buildMovieReadDto();

        assertThat(actualResult).isEqualTo(expectedResult);
        verifyNoInteractions(moviePersonReadMapper, reviewReadMapper);
    }

    @Test
    void fullMap() {
        doReturn(MoviePersonReadDto.builder().build()).when(moviePersonReadMapper).fullMap(any());
        doReturn(ReviewReadDto.builder().build()).when(reviewReadMapper).fullMap(any());
        MovieReadDto expectedResult = buildMovieReadDto();

        MovieReadDto actualResult = movieReadMapper.fullMap(buildMovie());

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(moviePersonReadMapper).fullMap(any());
        verify(reviewReadMapper).fullMap(any());
    }

    private Movie buildMovie() {
        return Movie.builder()
                .id(1)
                .title("title")
                .releaseYear((short) 2022)
                .country("country")
                .genre(Genre.ACTION)
                .description("description")
                .moviePersons(List.of(new MoviePerson()))
                .reviews(List.of(new Review()))
                .build();
    }

    private MovieReadDto buildMovieReadDto() {
        return MovieReadDto.builder()
                .id(1)
                .title("title")
                .releaseYear((short) 2022)
                .country("country")
                .genre(Genre.ACTION)
                .description("description")
                .moviePersons(List.of(MoviePersonReadDto.builder().build()))
                .reviews(List.of(ReviewReadDto.builder().build()))
                .build();
    }
}