package edu.jcourse.service;

import com.querydsl.core.types.Predicate;
import edu.jcourse.database.entity.QMovie;
import edu.jcourse.database.querydsl.QPredicates;
import edu.jcourse.database.repository.MovieRepository;
import edu.jcourse.dto.movie.MovieCreateEditDto;
import edu.jcourse.dto.movie.MovieFilter;
import edu.jcourse.dto.movie.MovieReadDto;
import edu.jcourse.mapper.movie.MovieCreateEditMapper;
import edu.jcourse.mapper.movie.MovieReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieReadMapper movieReadMapper;
    private final MovieCreateEditMapper movieCreateEditMapper;

    public Page<MovieReadDto> findAll(MovieFilter filter, Pageable pageable) {
        Predicate predicate = QPredicates.builder()
                .add(filter.title(), QMovie.movie.title::containsIgnoreCase)
                .add(filter.releaseYear(), QMovie.movie.releaseYear::eq)
                .add(filter.country(), QMovie.movie.country::containsIgnoreCase)
                .add(filter.genre(), QMovie.movie.genre::eq)
                .buildAnd();

        MovieFilter.Sort sortBy = Optional.ofNullable(filter.sortBy())
                .orElse(MovieFilter.Sort.TITLE);

        Sort sort = Sort.by(sortBy.getName()).ascending();
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        return movieRepository.findAll(predicate, pageRequest)
                .map(movieReadMapper::map);
    }

    public Optional<MovieReadDto> findById(Integer id) {
        return movieRepository.findById(id)
                .map(movieReadMapper::fullMap);
    }

    @Transactional
    public MovieReadDto create(MovieCreateEditDto movie) {
        return Optional.of(movie)
                .map(movieCreateEditMapper::map)
                .map(movieRepository::save)
                .map(movieReadMapper::map)
                .orElseThrow();
    }

    public Optional<MovieReadDto> findByAllFields(MovieFilter filter) {
        Predicate predicate = QPredicates.builder()
                .add(filter.title(), QMovie.movie.title::equalsIgnoreCase)
                .add(filter.releaseYear(), QMovie.movie.releaseYear::eq)
                .add(filter.country(), QMovie.movie.country::equalsIgnoreCase)
                .add(filter.genre(), QMovie.movie.genre::eq)
                .buildAnd();

        return movieRepository.findOne(predicate)
                .map(movieReadMapper::map);
    }

    @Transactional
    public Optional<MovieReadDto> update(Integer id, MovieCreateEditDto movieDto) {
        return movieRepository.findById(id)
                .map(movie -> movieCreateEditMapper.map(movieDto, movie))
                .map(movieRepository::saveAndFlush)
                .map(movieReadMapper::map);
    }

    @Transactional
    public boolean delete(Integer id) {
        return movieRepository.findById(id)
                .map(entity -> {
                    movieRepository.delete(entity);
                    movieRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}