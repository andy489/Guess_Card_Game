package com.cayetano.guesscard.model.enumerated;

import lombok.Getter;

@Getter
public enum ChipEnum {
    ONE("1", 1, "chip-1"),
    TWENTY_FIVE("25", 25, "chip-25"),
    HUNDRED("100", 100, "chip-100");

    private final String asTxt;

    private final Integer asNum;

    private final String imgName;

    ChipEnum(String asTxt, Integer asNum, String imgName) {
        this.asTxt = asTxt;
        this.asNum = asNum;
        this.imgName = imgName;
    }
}
