package edu.jcourse.mapper.person;

import edu.jcourse.database.entity.Person;
import edu.jcourse.dto.person.PersonCreateEditDto;
import edu.jcourse.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class PersonCreateEditMapper implements Mapper<PersonCreateEditDto, Person> {
    @Override
    public Person map(PersonCreateEditDto from) {
        Person to = new Person();
        copy(from, to);
        return to;
    }

    @Override
    public Person map(PersonCreateEditDto from, Person to) {
        copy(from, to);
        return to;
    }

    private void copy(PersonCreateEditDto from, Person to) {
        to.setName(from.name());
        to.setBirthDate(from.birthDate());
    }
}