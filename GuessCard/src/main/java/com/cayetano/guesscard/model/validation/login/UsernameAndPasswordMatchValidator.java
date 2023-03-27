package com.cayetano.guesscard.model.validation.login;

import com.cayetano.guesscard.model.entity.UserEntity;
import com.cayetano.guesscard.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.Optional;

public class UsernameAndPasswordMatchValidator implements ConstraintValidator<UsernameAndPasswordMatch, Object> {

    private String uniqueField;

    private String passwordField;

    private String message;

    private final UserService userService;

    private final PasswordEncoder encoder;

    public UsernameAndPasswordMatchValidator(
            UserService userService,
            PasswordEncoder encoder
    ) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @Override
    public void initialize(UsernameAndPasswordMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);

        this.uniqueField = constraintAnnotation.uniqueField();
        this.passwordField = constraintAnnotation.passwordField();

        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(value);

        String uniqueFieldValue = Objects.requireNonNull(beanWrapper.getPropertyValue(this.uniqueField)).toString();
        String passwordFieldValue = Objects.requireNonNull(beanWrapper.getPropertyValue(this.passwordField)).toString();

        boolean valid = true;

        Optional<UserEntity> byUsername = userService.getByUsername(uniqueFieldValue);

        if (byUsername.isEmpty()) {
            valid = false;
        }

        if (valid) {
            String encodedPassword = byUsername.get().getPassword();
            valid = encoder.matches(passwordFieldValue, encodedPassword);
        }

        if (!valid) {
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(passwordField)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return valid;
    }
}