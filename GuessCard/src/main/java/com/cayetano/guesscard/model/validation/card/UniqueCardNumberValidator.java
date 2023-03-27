package com.cayetano.guesscard.model.validation.card;

import com.cayetano.guesscard.service.CardService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueCardNumberValidator implements ConstraintValidator<UniqueCardNumber, String> {

    private final CardService cardService;

    public UniqueCardNumberValidator(CardService cardService) {
        this.cardService = cardService;
    }

    @Override
    public boolean isValid(String cardNumber, ConstraintValidatorContext context) {
        return cardService.getCardByCardNumber(cardNumber).isEmpty();
    }

}
