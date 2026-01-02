package com.cayetano.guesscard.web;

import com.cayetano.guesscard.engine.BetOdds;
import com.cayetano.guesscard.engine.Engine;
import com.cayetano.guesscard.engine.PlayingCard;
import com.cayetano.guesscard.engine.Specifics;
import com.cayetano.guesscard.helper.ErrorHelper;
import com.cayetano.guesscard.model.entity.GameEntity;
import com.cayetano.guesscard.model.enumerated.ChipEnum;
import com.cayetano.guesscard.service.GameService;
import com.cayetano.guesscard.service.UserService;
import com.cayetano.guesscard.session.CurrentGame;
import com.cayetano.guesscard.session.CurrentUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public final class HomeController extends GenericController {

    private final CurrentUser currentUser;

    private final CurrentGame currentGame;

    private final UserService userService;

    private final GameService gameService;

    @Autowired
    public HomeController(
            CurrentUser currentUser,
            CurrentGame currentGame,
            UserService userService,
            GameService gameService) {
        this.currentUser = currentUser;
        this.currentGame = currentGame;
        this.userService = userService;
        this.gameService = gameService;
    }

    @GetMapping({"/", "/index"})
    public ModelAndView getIndex() {
        if (currentUser.isLoggedIn()) {
            return super.redirect("/home");
        }

        return super.view("index");
    }

    @GetMapping("/home")
    public ModelAndView getHome(ModelAndView mav) {
        if (!currentUser.isLoggedIn()) {
            return super.redirect("/index");
        }

        BetOdds odds = gameService.getOdds();

        if (odds != null) {
            mav.addObject("odds", gameService.getOdds());
        }

        mav.setViewName("home");

        return mav;
    }

    @GetMapping("/pull")
    public ModelAndView pullCard(ModelAndView mav) throws JsonProcessingException {
        if (!currentUser.isLoggedIn()) {
            return super.redirect("/home");
        }

        BetOdds pull = gameService.pull();

        mav.addObject("odds", pull);

        return super.view("home", mav);
    }

    @GetMapping("/bet/{chipValue}")
    public ModelAndView chipBet(ModelAndView mav, @PathVariable("chipValue") String chipValue) throws JsonProcessingException {
        if (!currentUser.isLoggedIn()) {
            return super.redirect("/home");
        }

        List<String> validChips = new ArrayList<>();

        Arrays.stream(ChipEnum.values()).forEach((c) -> validChips.add(c.getAsTxt()));

        if (!validChips.contains(chipValue)) {
            return super.view("home");
        }

        if (Specifics.equals(currentGame.getBalance(), BigDecimal.ZERO) && Specifics.equals(currentGame.getBet(), BigDecimal.ZERO)) {
            ErrorHelper errorHelper = new ErrorHelper();
            errorHelper.toggleMustDepositCheck();
            mav.addObject("errorHelper", errorHelper);
            return super.view("home", mav);
        }

        gameService.chipBet(chipValue);

        return super.redirect("/home");
    }

    @GetMapping("/clear-bet")
    public ModelAndView clearBet(ModelAndView mav) {
        if (!currentUser.isLoggedIn()) {
            return super.redirect("/home");
        }

        userService.clearBet();


        return super.view("home");
    }

    @GetMapping("/reset-game")
    public ModelAndView resetGame(ModelAndView mav) throws JsonProcessingException {
        if (!currentUser.isLoggedIn()) {
            return super.redirect("/home");
        }

        userService.resetGame();


        return super.view("home");
    }

    @GetMapping("/gamble")
    public ModelAndView gamble(ModelAndView mav, @RequestParam("prediction") String prediction) throws JsonProcessingException {
        if (!currentUser.isLoggedIn()) {
            return super.redirect("/home");
        }

        if (Specifics.equals(currentGame.getBalance(), BigDecimal.ZERO) && Specifics.equals(currentGame.getBet(), BigDecimal.ZERO)) {
            mav.addObject("errorHelper", new ErrorHelper().toggleMustDepositCheck());
            return super.view("home", mav);
        }

        if (!Specifics.equals(currentGame.getBalance(), BigDecimal.ZERO) && Specifics.equals(currentGame.getBet(), BigDecimal.ZERO)) {
            mav.addObject("errorHelper", new ErrorHelper().toggleMustPlaceBetCheck());
            return super.view("home", mav);
        }

        if ("Joker".equals(currentGame.getPlayerCard().numeral())) {
            mav.addObject("errorHelper", new ErrorHelper().toggleMustPullCardCheck());
            return super.view("home", mav);
        }

        PlayingCard dealerCard = Engine.getRandomCardFromDeck(currentGame.getDeck());

        if (dealerCard.numeral().equals("Joker")) {
            return super.view("home", mav);
        }

        PlayingCard playerCard = currentGame.getPlayerCard();

        GameEntity gameEntity = gameService.getCurrentGame();

        BigDecimal win;

        if ("over".equals(prediction)) {

            if (playerCard.numeral().compareTo(dealerCard.numeral()) < 0) {
                win = gameEntity.getBet().multiply(new BigDecimal("2"));
            } else if (playerCard.numeral().compareTo(dealerCard.numeral()) == 0) {
                win = gameEntity.getBet().multiply(new BigDecimal("1"));
            } else {
                win = gameEntity.getBet().multiply(new BigDecimal("0"));
            }
        } else if ("under".equals(prediction)) {

            if (playerCard.numeral().compareTo(dealerCard.numeral()) > 0) {
                win = gameEntity.getBet().multiply(new BigDecimal("2"));
            } else if (playerCard.numeral().compareTo(dealerCard.numeral()) == 0) {
                win = gameEntity.getBet().multiply(new BigDecimal("1"));
            } else {
                win = gameEntity.getBet().multiply(new BigDecimal("0"));
            }
        } else {
            return super.view("home");
        }

        gameService.resolveBet(gameEntity, win, dealerCard, currentGame.getDeck());

        return super.view("home", mav);

    }


}