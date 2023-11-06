package edu.jcourse.service;

import edu.jcourse.database.repository.PersonRepository;
import edu.jcourse.dto.person.PersonReadDto;
import edu.jcourse.mapper.person.PersonReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}