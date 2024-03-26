package edu.jcourse.service;

import com.querydsl.core.types.Predicate;
import edu.jcourse.database.entity.Person;
import edu.jcourse.database.entity.QPerson;
import edu.jcourse.database.querydsl.QPredicates;
import edu.jcourse.database.repository.PersonRepository;
import edu.jcourse.dto.person.PersonCreateEditDto;
import edu.jcourse.dto.person.PersonFilter;
import edu.jcourse.dto.person.PersonReadDto;
import edu.jcourse.mapper.person.PersonCreateEditMapper;
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonReadMapper personReadMapper;
    private final PersonCreateEditMapper personCreateEditMapper;

    public List<PersonReadDto> findAll() {
        Sort sort = Sort.by(Person.Fields.name).ascending();
        return personRepository.findAll(sort).stream()
                .map(personReadMapper::map)
                .toList();
    }

    public Page<PersonReadDto> findAll(PersonFilter filter, Pageable pageable) {
        Predicate predicate = QPredicates.builder()
                .add(filter.name(), QPerson.person.name::containsIgnoreCase)
                .add(filter.birthYear(), QPerson.person.birthDate.year()::in)
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

    @Transactional
    public PersonReadDto create(PersonCreateEditDto person) {
        return Optional.of(person)
                .map(personCreateEditMapper::map)
                .map(personRepository::save)
                .map(personReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<PersonReadDto> update(Integer id, PersonCreateEditDto person) {
        return personRepository.findById(id)
                .map(p -> personCreateEditMapper.map(person, p))
                .map(personRepository::saveAndFlush)
                .map(personReadMapper::map);
    }

    @Transactional
    public boolean delete(Integer id) {
        return personRepository.findById(id)
                .map(entity -> {
                    personRepository.delete(entity);
                    personRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    public Optional<PersonReadDto> findByAllFields(PersonFilter filter) {
        Predicate predicate = QPredicates.builder()
                .add(filter.name(), QPerson.person.name::equalsIgnoreCase)
                .add(filter.birthDate(), QPerson.person.birthDate::eq)
                .buildAnd();

        return personRepository.findOne(predicate)
                .map(personReadMapper::map);
    }
}