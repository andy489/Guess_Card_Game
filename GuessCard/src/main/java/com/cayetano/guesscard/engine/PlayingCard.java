package com.cayetano.guesscard.engine;

public record PlayingCard(String numeral, String suit) {

    public static PlayingCard of(String numeral, String suit) {
        return new PlayingCard(numeral, suit);
    }

    @Override
    public String toString() {
        return numeral + "_of_" + suit;
    }
}
