package com.example.int221backend.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;

@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PublishAndCloseDateValidator.class)
@Documented
public @interface ValidPublishAndCloseDate {

    String message() default "Invalid publish and close date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}