package edu.jcourse.mapper.person;

import edu.jcourse.database.entity.Person;
import edu.jcourse.dto.person.PersonReadDto;
import edu.jcourse.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class PersonReadMapper implements Mapper<Person, PersonReadDto> {
    @Override
    public PersonReadDto map(Person from) {
        return PersonReadDto.builder()
                .id(from.getId())
                .name(from.getName())
                .birthDate(from.getBirthDate())
                .build();
    }
}