package edu.jcourse.validation.impl;

import edu.jcourse.service.UserService;
import edu.jcourse.validation.EmailInfo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailInfoValidator implements ConstraintValidator<EmailInfo, String> {

    private final UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && userService.findByEmail(value).isEmpty();
    }
}