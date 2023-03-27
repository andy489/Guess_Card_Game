package com.cayetano.guesscard.engine;

import java.math.BigDecimal;

public class Specifics {

    public static final BigDecimal epsilon = new BigDecimal("0.00001");

    public static boolean equals(BigDecimal a, BigDecimal b) {
        return a.subtract(b).compareTo(epsilon) < 1;
    }
}
