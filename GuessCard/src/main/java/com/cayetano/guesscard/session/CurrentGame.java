package com.cayetano.guesscard.session;

import com.cayetano.guesscard.engine.Engine;
import com.cayetano.guesscard.engine.PlayingCard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.LinkedList;

@Component
@SessionScope
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class CurrentGame {
    private PlayingCard playerCard;

    private PlayingCard dealerCard;

    private BigDecimal balance;

    private BigDecimal bet;

    private BigDecimal lastWin;

    private LinkedList<PlayingCard> deck;


    public void pull() {
        if (playerCard.equals(Engine.getDefaultPlayerCard())) {
            this.playerCard = Engine.getRandomCardFromDeck(deck);
        }
    }

    public void clear() {
        this.bet = null;
        this.balance = null;
        this.dealerCard = null;
        this.playerCard = null;
        this.deck = null;
    }
}
