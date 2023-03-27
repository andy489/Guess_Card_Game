package com.cayetano.guesscard.service;

import com.cayetano.guesscard.engine.BetOdds;
import com.cayetano.guesscard.engine.Engine;
import com.cayetano.guesscard.engine.PlayingCard;
import com.cayetano.guesscard.mapper.MapStructMapper;
import com.cayetano.guesscard.model.entity.GameEntity;
import com.cayetano.guesscard.repository.GameRepository;
import com.cayetano.guesscard.session.CurrentGame;
import com.cayetano.guesscard.session.CurrentUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.NoSuchElementException;

@Service
public final class GameService {

    private final GameRepository gameRepository;

    private final MapStructMapper mapper;

    private final CurrentGame currentGame;

    private final CurrentUser currentUser;

    public GameService(GameRepository gameRepository,
                       MapStructMapper mapper,
                       CurrentGame currentGame,
                       CurrentUser currentUser) {
        this.gameRepository = gameRepository;
        this.mapper = mapper;
        this.currentGame = currentGame;
        this.currentUser = currentUser;
    }

    public GameEntity getCurrentGame() {
        return gameRepository.findByOwnerId(currentUser.getId()).orElseThrow(NoSuchElementException::new);
    }

    public BetOdds pull() throws JsonProcessingException {

        currentGame.pull();

        BetOdds betPredictions = getOdds();

        GameEntity thisGame = getCurrentGame();

        mapper.updateGameEntity(thisGame, currentGame);
        gameRepository.save(thisGame);

        return betPredictions;
    }

    public void chipBet(String chipValue) throws JsonProcessingException {

        BigDecimal bet = new BigDecimal(chipValue);

        GameEntity thisGame = getCurrentGame();

        thisGame.placeBet(bet);

        GameEntity savedGame = gameRepository.save(thisGame);

        mapper.updateSessionGame(currentGame, savedGame);
    }


    public BetOdds getOdds() {
        return Engine.calcStatistics(currentGame.getDeck(), currentGame.getPlayerCard());
    }

    public void resolveBet(GameEntity gameEntity, BigDecimal win, PlayingCard dealerCard, LinkedList<PlayingCard> deck) throws JsonProcessingException {
        gameEntity.setLastWin(win);
        gameEntity.setBalance(gameEntity.getBalance().add(win));
        gameEntity.clearBet();

        gameEntity.setDealerCard(Engine.om.writeValueAsString(dealerCard));
        gameEntity.setDeck(Engine.om.writeValueAsString(deck));

        gameRepository.save(gameEntity);

        mapper.updateSessionGame(currentGame, gameEntity);
    }

    public void save(GameEntity thisGame) {
        gameRepository.save(thisGame);
    }
}
