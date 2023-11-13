package edu.jcourse.service;

import com.querydsl.core.types.Predicate;
import edu.jcourse.database.querydsl.QPredicates;
import edu.jcourse.database.repository.PersonRepository;
import edu.jcourse.dto.person.PersonFilter;
import edu.jcourse.dto.person.PersonReadDto;
import edu.jcourse.mapper.person.PersonReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static edu.jcourse.database.entity.QPerson.person;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonReadMapper personReadMapper;

    public List<PersonReadDto> findAll() {
        Sort sort = Sort.by("name").ascending();
        return personRepository.findAll(sort).stream()
                .map(personReadMapper::map)
                .toList();
    }

    public Page<PersonReadDto> findAll(PersonFilter filter, Pageable pageable) {
        Predicate predicate = QPredicates.builder()
                .add(filter.name(), person.name::containsIgnoreCase)
                .add(filter.birthYear(), person.birthDate.year()::in)
                .buildAnd();

        PersonFilter.Sort sortBy = Optional.ofNullable(filter.sortBy())
                .orElse(PersonFilter.Sort.NAME);

        Sort sort = Sort.by(sortBy.getName()).ascending();
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        return personRepository.findAll(predicate, pageRequest)
                .map(personReadMapper::map);
    }

    public Optional<PersonReadDto> findById(Integer id) {
        return personRepository.findById(id)
                .map(personReadMapper::fullMap);
    }
}