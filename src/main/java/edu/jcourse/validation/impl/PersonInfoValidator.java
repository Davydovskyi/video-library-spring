package edu.jcourse.validation.impl;

import edu.jcourse.dto.person.PersonCreateEditDto;
import edu.jcourse.dto.person.PersonFilter;
import edu.jcourse.service.PersonService;
import edu.jcourse.validation.PersonInfo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Objects;

@RequiredArgsConstructor
public class PersonInfoValidator implements ConstraintValidator<PersonInfo, PersonCreateEditDto> {

    private final PersonService personService;

    @Override
    public boolean isValid(PersonCreateEditDto value, ConstraintValidatorContext context) {
        if (isValid(value)) {
            PersonFilter filter = PersonFilter.builder()
                    .name(value.name())
                    .birthDate(value.birthDate())
                    .build();
            return personService.findByAllFields(filter).isEmpty();
        }

        return true;
    }

    private boolean isValid(PersonCreateEditDto value) {
        return StringUtils.hasText(value.name()) &&
                Objects.nonNull(value.birthDate());
    }
}