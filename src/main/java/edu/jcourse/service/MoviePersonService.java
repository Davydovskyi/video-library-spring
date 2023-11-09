package edu.jcourse.service;

import com.querydsl.core.types.Predicate;
import edu.jcourse.database.entity.MoviePerson;
import edu.jcourse.database.querydsl.QPredicates;
import edu.jcourse.database.repository.MoviePersonRepository;
import edu.jcourse.dto.movieperson.MoviePersonCreateEditDto;
import edu.jcourse.dto.movieperson.MoviePersonFilter;
import edu.jcourse.dto.movieperson.MoviePersonReadDto;
import edu.jcourse.mapper.movieperson.MoviePersonCreateEditMapper;
import edu.jcourse.mapper.movieperson.MoviePersonReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.Optional;

import static edu.jcourse.database.entity.QMoviePerson.moviePerson;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MoviePersonService {

    private final MoviePersonRepository moviePersonRepository;
    private final MoviePersonCreateEditMapper moviePersonCreateEditMapper;
    private final MoviePersonReadMapper moviePersonReadMapper;

    @Transactional
    public MoviePersonReadDto create(MoviePersonCreateEditDto moviePerson) {
        return Optional.of(moviePerson)
                .map(moviePersonCreateEditMapper::map)
                .map(moviePersonRepository::save)
                .map(moviePersonReadMapper::map)
                .orElseThrow();
    }

    public Optional<MoviePersonReadDto> findByAllFields(MoviePersonFilter filter) {
        Predicate predicate = QPredicates.builder()
                .add(filter.personId(), moviePerson.person.id::eq)
                .add(filter.movieId(), moviePerson.movie.id::eq)
                .add(filter.role(), moviePerson.personRole::eq)
                .buildAnd();
        Iterator<MoviePerson> iterator = moviePersonRepository.findAll(predicate).iterator();
        return iterator.hasNext() ? Optional.of(moviePersonReadMapper.map(iterator.next())) : Optional.empty();
    }
}