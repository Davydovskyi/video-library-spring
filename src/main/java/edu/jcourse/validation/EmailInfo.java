package edu.jcourse.validation;


import edu.jcourse.validation.impl.EmailInfoValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = EmailInfoValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface EmailInfo {
    String message() default "{registration.error.email.exist}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}