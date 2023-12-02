package edu.jcourse.validation.impl;

import edu.jcourse.database.entity.User;
import edu.jcourse.service.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class EmailInfoValidatorTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private EmailInfoValidator emailInfoValidator;

    static Stream<Arguments> getEmailValidationArguments() {
        return Stream.of(
                Arguments.of(" ", true),
                Arguments.of("", true),
                Arguments.of(null, true),
                Arguments.of("test", true),
                Arguments.of("test@test", false));
    }

    @ParameterizedTest
    @MethodSource("getEmailValidationArguments")
    void isValid(String email, boolean expectedResult) {
        lenient().doReturn(Optional.of(new User())).when(userService).findByEmail("test@test");

        assertThat(emailInfoValidator.isValid(email, null)).isEqualTo(expectedResult);
    }
}