package com.cayetano.guesscard.mapper;

import com.cayetano.guesscard.engine.PlayingCard;
import com.cayetano.guesscard.model.dto.CardAddDto;
import com.cayetano.guesscard.model.dto.UserRegisterDto;
import com.cayetano.guesscard.model.entity.CardEntity;
import com.cayetano.guesscard.model.entity.GameEntity;
import com.cayetano.guesscard.model.entity.UserEntity;
import com.cayetano.guesscard.session.CurrentGame;
import com.cayetano.guesscard.session.CurrentUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.LinkedList;

@Mapper(componentModel = "spring")
public interface MapStructMapper {

    ObjectMapper om = new ObjectMapper();

    @Mapping(target = "id", ignore = true)
    UserEntity toUserEntity(UserRegisterDto userRegisterDto);

    @Mapping(target = "id", ignore = true)
    CardEntity toCardEntity(CardAddDto cardAddDto);

    default void toCurrentGame(CurrentGame currentGame, GameEntity gameEntity) throws JsonProcessingException {

        currentGame.setPlayerCard(toObj(gameEntity.getPlayerCard()));
        currentGame.setDealerCard(toObj(gameEntity.getDealerCard()));
        currentGame.setBalance(gameEntity.getBalance());
        currentGame.setBet(gameEntity.getBet());
        currentGame.setLastWin(gameEntity.getLastWin());
        currentGame.setDeck(toListOfObj(gameEntity.getDeck()));

    }

    default void updateSessionUser(CurrentUser currentUser, UserEntity userEntity) {

        currentUser.setId(userEntity.getId());
        currentUser.setUsername(userEntity.getUsername());
        currentUser.setFullName(userEntity.getFullName());
        currentUser.setEmail(userEntity.getEmail());
    }


    default void updateGameEntity(GameEntity gameEntity, CurrentGame currentGame) throws JsonProcessingException {
        gameEntity.setBet(currentGame.getBet());
        gameEntity.setBalance(currentGame.getBalance());
        gameEntity.setLastWin(currentGame.getLastWin());

        gameEntity.setPlayerCard(toJson(currentGame.getPlayerCard()));
        gameEntity.setDealerCard(toJson(currentGame.getDealerCard()));

        gameEntity.setDeck(toJson(currentGame.getDeck()));
    }

    default void updateSessionGame(CurrentGame currentGame, GameEntity gameEntity) throws JsonProcessingException {
        currentGame.setBet(gameEntity.getBet());
        currentGame.setBalance(gameEntity.getBalance());
        currentGame.setLastWin(gameEntity.getLastWin());

        currentGame.setPlayerCard(toObj(gameEntity.getPlayerCard()));
        currentGame.setDealerCard(toObj(gameEntity.getDealerCard()));

        currentGame.setDeck(toListOfObj(gameEntity.getDeck()));
    }


    default PlayingCard toObj(String playingCard) throws JsonProcessingException {
        return om.readValue(playingCard, PlayingCard.class);
    }

    default String toJson(PlayingCard playingCard) throws JsonProcessingException {
        return om.writeValueAsString(playingCard);
    }

    default LinkedList<PlayingCard> toListOfObj(String deck) throws JsonProcessingException {
        return om.readValue(deck, new TypeReference<>() {
        });
    }

    default String toJson(LinkedList<PlayingCard> deck) throws JsonProcessingException {
        return om.writeValueAsString(deck);
    }
}
