package edu.jcourse.service;

import com.querydsl.core.types.Predicate;
import edu.jcourse.database.querydsl.QPredicates;
import edu.jcourse.database.repository.ReviewRepository;
import edu.jcourse.dto.review.ReviewCreateEditDto;
import edu.jcourse.dto.review.ReviewFilter;
import edu.jcourse.dto.review.ReviewReadDto;
import edu.jcourse.mapper.review.ReviewCreateEditMapper;
import edu.jcourse.mapper.review.ReviewReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static edu.jcourse.database.entity.QReview.review;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewReadMapper reviewReadMapper;
    private final ReviewCreateEditMapper reviewCreateEditMapper;

    public List<ReviewReadDto> findAllByUserId(Long userId) {
        return reviewRepository.findAllByUserId(userId).stream()
                .map(reviewReadMapper::fullMap)
                .toList();
    }

    public Optional<ReviewReadDto> findByAllFields(ReviewFilter filter) {
        Predicate predicate = QPredicates.builder()
                .add(filter.movieId(), review.movie.id::eq)
                .add(filter.userId(), review.user.id::eq)
                .buildAnd();
        return reviewRepository.findOne(predicate)
                .map(reviewReadMapper::map);
    }

    @Transactional
    public ReviewReadDto create(ReviewCreateEditDto review) {
        return Optional.of(review)
                .map(reviewCreateEditMapper::map)
                .map(reviewRepository::save)
                .map(reviewReadMapper::map)
                .orElseThrow();
    }
}