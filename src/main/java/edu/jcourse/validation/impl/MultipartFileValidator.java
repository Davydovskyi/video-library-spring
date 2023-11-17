package edu.jcourse.validation.impl;

import edu.jcourse.util.RegexUtil;
import edu.jcourse.validation.MultipartFileInfo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class MultipartFileValidator implements ConstraintValidator<MultipartFileInfo, MultipartFile> {

    private static final Long IMAGE_SIZE = 1024L * 1024;

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (Objects.nonNull(value) && !value.isEmpty()) {
            return value.getSize() <= IMAGE_SIZE
                    && Objects.requireNonNull(value.getOriginalFilename()).matches(RegexUtil.IMAGE_FORMAT_REGEX);
        }
        return true;
    }
}