package edu.jcourse.validation;

import edu.jcourse.validation.impl.MovieInfoValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MovieInfoValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MovieInfo {
    String message() default "{movie.add.error.exists}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
