package com.cayetano.guesscard.service;

import com.cayetano.guesscard.engine.Engine;
import com.cayetano.guesscard.mapper.MapStructMapper;
import com.cayetano.guesscard.model.dto.CardAddDto;
import com.cayetano.guesscard.model.entity.CardEntity;
import com.cayetano.guesscard.model.entity.GameEntity;
import com.cayetano.guesscard.repository.CardRepository;
import com.cayetano.guesscard.session.CurrentGame;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public final class CardService {

    private final CardRepository cardRepository;

    private final UserService userService;

    private final GameService gameService;

    private final MapStructMapper mapper;

    private final CurrentGame currentGame;

    @Autowired
    public CardService(
            CardRepository cardRepository,
            UserService userService,
            GameService gameService,
            MapStructMapper mapper,
            CurrentGame currentGame) {

        this.cardRepository = cardRepository;
        this.userService = userService;
        this.gameService = gameService;
        this.mapper = mapper;
        this.currentGame = currentGame;
    }

    public void addCard(CardAddDto cardAddDto) {

        CardEntity cardEntity = mapper.toCardEntity(cardAddDto);
        cardEntity.setOwner(userService.getCurrentUser());

        CardEntity save = cardRepository.saveAndFlush(cardEntity);
    }

    public Optional<CardEntity> getCardByUserId(Long id) {
        return cardRepository.findByOwnerId(id);
    }

    public Optional<CardEntity> getCardByCardNumber(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber);
    }

    public void cashOut() throws JsonProcessingException {

        GameEntity thisGame = gameService.getCurrentGame();

        thisGame.setBalance(thisGame.getBalance().add(thisGame.getBet()));
        thisGame.setBet(Engine.getZero());
        thisGame.setBalance(Engine.getZero());

        mapper.updateSessionGame(currentGame, thisGame);

        gameService.save(thisGame);
    }
}
