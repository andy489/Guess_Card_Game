package com.cayetano.guesscard.model.validation.card;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = UniqueCardNumberValidator.class)
public @interface UniqueCardNumber {

    String message() default "invalid card number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}