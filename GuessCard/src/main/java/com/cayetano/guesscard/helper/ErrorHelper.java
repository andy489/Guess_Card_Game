package com.cayetano.guesscard.helper;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
public class ErrorHelper {
    private Boolean mustDepositCheck;
    private final String mustDeposit;

    private Boolean mustPlaceBetCheck;

    private final String mustPlaceBet;

    private Boolean mustPullCardCheck;

    private final String mustPullCard;

    public ErrorHelper() {
        this.mustDepositCheck = false;
        this.mustPlaceBetCheck = false;
        this.mustPullCardCheck = false;

        this.mustDeposit = "Insufficient funds! Please, deposit first.";
        this.mustPlaceBet = "No bet has been placed. You must place your bet!";
        this.mustPullCard = "No player card has been pulled. You must pull a card!";
    }

    public ErrorHelper toggleMustDepositCheck() {
        mustDepositCheck = !mustDepositCheck;
        return this;
    }

    public ErrorHelper toggleMustPlaceBetCheck() {
        mustPlaceBetCheck = !mustPlaceBetCheck;
        return this;
    }

    public ErrorHelper toggleMustPullCardCheck() {
        mustPullCardCheck = !mustPullCardCheck;
        return this;
    }


    public Boolean hasError() {
        return mustDepositCheck || mustPlaceBetCheck || mustPullCardCheck;
    }
}
