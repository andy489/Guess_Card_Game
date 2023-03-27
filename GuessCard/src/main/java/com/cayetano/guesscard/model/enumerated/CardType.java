package com.cayetano.guesscard.model.enumerated;

import lombok.Getter;

@Getter
public enum CardType {
    MASTER_CARD("Master Card"),
    VISA_CARD("Visa Card"),
    DEBIT_CARD("Debit Card"),
    CREDIT_CARD("Credit Card"),
    DISPOSAL_REVOLUT_CARD("Disposal Revolut Card");

    private final String displayName;

    CardType(String displayName) {
        this.displayName = displayName;
    }
}
