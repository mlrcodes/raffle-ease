package com.raffleease.raffles_service.Validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TicketsLimitsValidator.class)
public @interface ValidTicketsLimits {
    String message() default "The upper limit must be greater than the lower limit for tickets.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}