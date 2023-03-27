package com.cayetano.guesscard.model.validation.login;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = UsernameAndPasswordMatchValidator.class)
public @interface UsernameAndPasswordMatch {
    String uniqueField();

    String passwordField();


    String message() default "failed to login";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
