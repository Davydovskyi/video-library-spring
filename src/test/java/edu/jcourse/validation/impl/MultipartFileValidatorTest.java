package edu.jcourse.validation.impl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class MultipartFileValidatorTest {

    private final MultipartFileValidator validator = new MultipartFileValidator();

    static Stream<Arguments> getMultipartFileValidationArguments() {
        return Stream.of(
                Arguments.of(null, true),
                Arguments.of(getTrainedMultipartFile(true, true, true), true),
                Arguments.of(getTrainedMultipartFile(false, true, true), true),
                Arguments.of(getTrainedMultipartFile(true, false, true), true),
                Arguments.of(getTrainedMultipartFile(true, true, false), true),
                Arguments.of(getTrainedMultipartFile(false, true, false), false),
                Arguments.of(getTrainedMultipartFile(true, false, false), true),
                Arguments.of(getTrainedMultipartFile(false, false, false), false)
        );
    }

    private static MultipartFile getTrainedMultipartFile(boolean isEmpty, boolean isSizeValid, boolean isNameValid) {
        MultipartFile value = mock(MultipartFile.class);

        doReturn(isEmpty).when(value).isEmpty();

        long size = isSizeValid ? 2345L : 1025L * 1024;
        doReturn(size).when(value).getSize();

        String filename = isNameValid ? "image.jpg" : "image.tys";
        doReturn(filename).when(value).getOriginalFilename();

        return value;
    }

    @ParameterizedTest
    @MethodSource("getMultipartFileValidationArguments")
    void isValid(MultipartFile file, boolean expectedResult) {

        assertThat(validator.isValid(file, null)).isEqualTo(expectedResult);
    }
}