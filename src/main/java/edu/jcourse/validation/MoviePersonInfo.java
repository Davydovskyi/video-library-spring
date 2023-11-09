package edu.jcourse.validation;

import edu.jcourse.validation.impl.MoviePersonInfoValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MoviePersonInfoValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MoviePersonInfo {
    String message() default "{movie.person.add.error.exists}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}