package edu.jcourse.validation;

import edu.jcourse.validation.impl.MultipartFileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = MultipartFileValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Documented
public @interface MultipartFileInfo {
    String message() default "{registration.error.image.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}