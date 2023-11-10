package edu.jcourse.validation;

import edu.jcourse.validation.impl.ReviewInfoValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ReviewInfoValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ReviewInfo {
    String message() default "{movie.review.add.error.exists}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
