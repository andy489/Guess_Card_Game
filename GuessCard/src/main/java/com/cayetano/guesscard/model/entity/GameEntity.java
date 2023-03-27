package com.cayetano.guesscard.model.entity;

import com.cayetano.guesscard.engine.Engine;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Entity(name = "uniqueGameEntity")
@Table(name = "games")
@Getter
@Setter
@Accessors(chain = true)
public class GameEntity extends GenericEntity {

    @Column(nullable = false)
    private String playerCard;

    @Column(nullable = false)
    private String dealerCard;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private BigDecimal bet;


    @Column(nullable = false)
    private BigDecimal lastWin;


    @Column(nullable = false, columnDefinition = "TEXT")
    private String deck;

    @OneToOne(fetch = FetchType.EAGER)
    public UserEntity owner;

    public GameEntity() throws JsonProcessingException {
        playerCard = Engine.om.writeValueAsString(Engine.getDefaultPlayerCard());
        dealerCard = Engine.om.writeValueAsString(Engine.getDefaultDealerCard());

        deck = Engine.om.writeValueAsString(Engine.generateDeck());

        balance = Engine.getZero();
        bet = Engine.getZero();
        lastWin = Engine.getZero();
    }


    public void addMoney(BigDecimal money) {
        balance = balance.add(money);
    }

    public void placeBet(BigDecimal money) {
        if (money.compareTo(balance) > 0) {
            bet = bet.add(balance);
            balance = Engine.getZero();
        } else {
            bet = bet.add(money);
            balance = balance.subtract(money);
        }
    }

    public void restoreBet() {
        balance = balance.add(bet);
        bet = Engine.getZero();
    }

    public void clearBet() {
        bet = Engine.getZero();
    }
}
