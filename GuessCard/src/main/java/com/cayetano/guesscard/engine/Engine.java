package com.cayetano.guesscard.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class Engine {

    public static ObjectMapper om = new ObjectMapper();

    public static final String[] suites = {"Clubs", "Diamonds", "Hearts", "Spades"};

    private static final String[] numerals = {"02", "03", "04", "05", "06", "07", "08", "09", "10", "11_Jake", "12_Queen", "13_King", "14_Ace"};

    private static final PlayingCard defaultPlayerCard = PlayingCard.of("00_Joker", "Red");

    private static final PlayingCard defaultDealerCard = PlayingCard.of("00_Joker", "Black");

    private static final BigDecimal ZERO = new BigDecimal("0.00");

    private static final int OVER_INDEX = 0;

    private static final int SAME_INDEX = 1;

    private static final int UNDER_INDEX = 2;

    private static final int TOTAL_OUTCOMES = 3;

    private static final int NUMERAL_POS = 0;

    public static PlayingCard getDefaultPlayerCard() {
        return defaultPlayerCard;
    }

    public static PlayingCard getDefaultDealerCard() {
        return defaultDealerCard;
    }

    public static BigDecimal getZero() {
        return ZERO;
    }


    public static LinkedList<PlayingCard> generateDeck() {
        return new LinkedList<>(Arrays.stream(numerals).flatMap(
                        n -> Arrays.stream(suites).map(s -> new PlayingCard(n, s)))
                .toList());
    }

    public static String generateNewDeckToPersist() throws JsonProcessingException {
        return om.writeValueAsString(generateDeck());
    }

    public static PlayingCard getRandomCardFromDeck(LinkedList<PlayingCard> deck) {

        Collections.shuffle(deck);

        Optional<PlayingCard> any = deck.stream().findFirst();

        if (any.isEmpty()) {
            return Engine.getDefaultDealerCard();
        }

        deck.remove(any.get());

        return any.get();
    }

    public static String getDefaultPlayerCardToPersist() throws JsonProcessingException {
        return om.writeValueAsString(getDefaultPlayerCard());
    }

    public static String getDefaultDealerCardToPersist() throws JsonProcessingException {
        return om.writeValueAsString(getDefaultDealerCard());
    }


    public static BetOdds calcStatistics(LinkedList<PlayingCard> deck, PlayingCard playerCard) {

        if (playerCard.numeral().equals("Joker")) {
            return null;
        }

        BetOdds betPredictions = new BetOdds();

        List<Integer> cntOverUnder = countOverSameUnder(deck, playerCard);

        Integer overCnt = cntOverUnder.get(OVER_INDEX);
        Integer sameCnt = cntOverUnder.get(SAME_INDEX);
        Integer underCnt = cntOverUnder.get(UNDER_INDEX);

        int totalCardsLeftInDeck = deck.size();

        betPredictions.setOverChance(overCnt * 1.0 / totalCardsLeftInDeck * 100);
        betPredictions.setUnderChance(underCnt * 1.0 / totalCardsLeftInDeck * 100);

        return betPredictions;
    }

    private static List<Integer> countOverSameUnder(LinkedList<PlayingCard> deck, PlayingCard playerCard) {
        List<Integer> cntOverSameUnder = new ArrayList<>();
        IntStream.rangeClosed(1, TOTAL_OUTCOMES).forEach(i -> cntOverSameUnder.add(0));

        String playerCardCardinality = playerCard.numeral().split("_")[NUMERAL_POS];

        for (PlayingCard p : deck) {

            String currCardCardinality = p.numeral().split("_")[NUMERAL_POS];

            if (currCardCardinality.compareTo(playerCardCardinality) == 0) {
                cntOverSameUnder.set(SAME_INDEX, cntOverSameUnder.get(SAME_INDEX) + 1);
            } else if (currCardCardinality.compareTo(playerCardCardinality) > 0) {
                cntOverSameUnder.set(OVER_INDEX, cntOverSameUnder.get(OVER_INDEX) + 1);
            } else {
                cntOverSameUnder.set(UNDER_INDEX, cntOverSameUnder.get(UNDER_INDEX) + 1);
            }
        }

        return cntOverSameUnder;
    }


}
