package com.cayetano.guesscard.model.validation.card;

import com.cayetano.guesscard.model.entity.CardEntity;
import com.cayetano.guesscard.model.entity.UserEntity;
import com.cayetano.guesscard.service.CardService;
import com.cayetano.guesscard.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.NoSuchElementException;

public class CardCVCMatcherValidator implements ConstraintValidator<CardCVCMatcher, String> {

    private final UserService userService;

    private final CardService cardService;

    public CardCVCMatcherValidator(UserService userService, CardService cardService) {
        this.userService = userService;
        this.cardService = cardService;
    }

    @Override
    public boolean isValid(String inputCvc, ConstraintValidatorContext context) {

        UserEntity currentUser = userService.getCurrentUser();

        CardEntity cardEntity = cardService.getCardByUserId(currentUser.getId()).orElseThrow(NoSuchElementException::new);

        String actualCvc = cardEntity.getCvc();

        boolean cvcEqual = actualCvc.equals(inputCvc);

        return cvcEqual;
    }
}
