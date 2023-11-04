package edu.jcourse.validation.impl;

import edu.jcourse.service.UserService;
import edu.jcourse.validation.EmailInfo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class EmailInfoValidator implements ConstraintValidator<EmailInfo, String> {

    private final UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.hasText(value)) {
            return userService.findByEmail(value).isEmpty();
        }
        return true;
    }
}