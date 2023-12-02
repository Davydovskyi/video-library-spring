package edu.jcourse.validation.impl;

import edu.jcourse.dto.person.PersonCreateEditDto;
import edu.jcourse.dto.person.PersonFilter;
import edu.jcourse.dto.person.PersonReadDto;
import edu.jcourse.service.PersonService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class PersonInfoValidatorTest {

    @Mock
    private PersonService personService;
    @InjectMocks
    private PersonInfoValidator personInfoValidator;

    static Stream<Arguments> getPersonValidationArguments() {
        return Stream.of(
                Arguments.of(buildCreateEditDto("name", LocalDate.now()), false),
                Arguments.of(buildCreateEditDto("name2", LocalDate.now()), true),
                Arguments.of(buildCreateEditDto("name", null), true),
                Arguments.of(buildCreateEditDto(null, LocalDate.now()), true),
                Arguments.of(buildCreateEditDto("", LocalDate.now()), true)
        );
    }

    private static PersonCreateEditDto buildCreateEditDto(String name, LocalDate birthDate) {
        return PersonCreateEditDto.builder()
                .name(name)
                .birthDate(birthDate)
                .build();
    }

    @ParameterizedTest
    @MethodSource("getPersonValidationArguments")
    void isValid(PersonCreateEditDto value, boolean expectedResult) {
        PersonFilter filter = new PersonFilter("name", null, LocalDate.now(), null);
        lenient().doReturn(Optional.of(PersonReadDto.builder().build())).when(personService).findByAllFields(filter);

        assertThat(personInfoValidator.isValid(value, null)).isEqualTo(expectedResult);
    }
}