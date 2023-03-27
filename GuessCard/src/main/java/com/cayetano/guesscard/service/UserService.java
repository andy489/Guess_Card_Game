package com.cayetano.guesscard.service;

import com.cayetano.guesscard.engine.Engine;
import com.cayetano.guesscard.mapper.MapStructMapper;
import com.cayetano.guesscard.model.dto.UserLoginDto;
import com.cayetano.guesscard.model.dto.UserRegisterDto;
import com.cayetano.guesscard.model.entity.CardEntity;
import com.cayetano.guesscard.model.entity.GameEntity;
import com.cayetano.guesscard.model.entity.UserEntity;
import com.cayetano.guesscard.repository.CardRepository;
import com.cayetano.guesscard.repository.GameRepository;
import com.cayetano.guesscard.repository.UserRepository;
import com.cayetano.guesscard.session.CurrentGame;
import com.cayetano.guesscard.session.CurrentUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public final class UserService {
    private final UserRepository userRepository;

    private final CardRepository cardRepository;

    private final GameRepository gameRepository;

    private final CurrentUser currentUser;

    private final CurrentGame currentGame;

    private final MapStructMapper mapper;

    private final PasswordEncoder encoder;

    @Autowired
    public UserService(
            UserRepository userRepository,
            CardRepository cardRepository,
            GameRepository gameRepository,
            CurrentUser currentUser,
            CurrentGame currentGame,
            MapStructMapper mapper,
            PasswordEncoder encoder
    ) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.gameRepository = gameRepository;
        this.currentUser = currentUser;
        this.currentGame = currentGame;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    public UserEntity getCurrentUser() {
        return userRepository.findById(currentUser.getId()).orElseThrow(NoSuchElementException::new);
    }

    public GameEntity getCurrentGame() {
        return gameRepository.findById(getCurrentUser().getId()).orElseThrow(NoSuchElementException::new);
    }

    public Optional<CardEntity> getCurrentUserCard(Long userId) {
        return cardRepository.findByOwnerId(userId);
    }

    public Optional<UserEntity> getById(Long id) {

        return userRepository.findById(id);
    }

    public UserEntity getByIdExisting(Long id) {
        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public Optional<UserEntity> getByUsername(String username) {


        return userRepository.findByUsername(username);
    }

    public Optional<UserEntity> getByEmail(String email) {


        return userRepository.findByEmail(email);
    }

    public void registerAndLogin(UserRegisterDto userRegisterDto) throws JsonProcessingException {

        UserEntity newUser = mapper.toUserEntity(userRegisterDto)
                .setPassword(encoder.encode(userRegisterDto.getPassword()));

        GameEntity newGame = new GameEntity();

        UserEntity savedNewUser = userRepository.save(newUser);
        newGame.setOwner(userRepository.save(savedNewUser));

        GameEntity savedNewGame = gameRepository.save(newGame);

        login(savedNewUser, savedNewGame);
    }

    private void login(UserEntity userEntity, GameEntity gameEntity) throws JsonProcessingException {

        mapper.updateSessionUser(currentUser, userEntity);
        currentUser.setLoggedIn(true);

        mapper.toCurrentGame(currentGame, gameEntity);
    }

    public void login(UserLoginDto userLoginDto) throws JsonProcessingException {

        UserEntity existingUser = userRepository.findByUsername(userLoginDto.getUsername())
                .orElseThrow(NoSuchElementException::new);

        GameEntity gameEntity = gameRepository.findByOwnerId(existingUser.getId()).orElseThrow(NoSuchElementException::new);

        login(existingUser, gameEntity);
    }

    public void logout() {

        UserEntity thisUser = getCurrentUser();
        GameEntity thisGame = getCurrentGame();

        thisGame.setBalance(thisGame.getBalance().add(thisGame.getBet()));
        thisGame.setBet(BigDecimal.ZERO);

        userRepository.save(thisUser);
        gameRepository.save(thisGame);

        currentUser.clear();
        currentGame.clear();
    }

    public void addMoney(String amount) throws JsonProcessingException {
        BigDecimal moneyToAdd = new BigDecimal(amount);

        GameEntity thisGame = getCurrentGame();

        thisGame.addMoney(moneyToAdd);
        resetCardsAndBetState(thisGame);
        GameEntity save = gameRepository.save(thisGame);

        mapper.updateSessionGame(currentGame, save);

    }

    public void clearBet() {

        GameEntity thisGame = getCurrentGame();

        thisGame.restoreBet();
        GameEntity save = gameRepository.save(thisGame);

        currentGame.setBalance(save.getBalance());
        currentGame.setBet(save.getBet());
    }

    public void resetGame() throws JsonProcessingException {
        GameEntity thisGame = getCurrentGame();

        resetCardsAndBetState(thisGame);
        GameEntity save = gameRepository.save(thisGame);

        mapper.updateSessionGame(currentGame, save);

    }

    private void resetCardsAndBetState(GameEntity thisGame) throws JsonProcessingException {
        thisGame.setPlayerCard(Engine.getDefaultPlayerCardToPersist());
        thisGame.setDealerCard(Engine.getDefaultDealerCardToPersist());

        thisGame.setLastWin(Engine.getZero());

        thisGame.setDeck(Engine.generateNewDeckToPersist());
        thisGame.restoreBet();
    }
}