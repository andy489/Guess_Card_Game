package com.cayetano.guesscard.web;

import com.cayetano.guesscard.model.dto.CardAddDto;
import com.cayetano.guesscard.model.dto.DepositDto;
import com.cayetano.guesscard.service.CardService;
import com.cayetano.guesscard.service.UserService;
import com.cayetano.guesscard.session.CurrentUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/card")
public final class CardController extends GenericController {

    private final CardService cardService;
    private final UserService userService;
    private final CurrentUser currentUser;

    @Autowired
    public CardController(
            CardService cardService,
            UserService userService,
            CurrentUser currentUser) {

        this.cardService = cardService;
        this.userService = userService;
        this.currentUser = currentUser;
    }

    @ModelAttribute(name = "cardAddModel")
    public CardAddDto initCardAddModel() {
        return new CardAddDto();
    }

    @ModelAttribute(name = "depositModel")
    public DepositDto makeDeposit() {
        return new DepositDto();
    }

    @GetMapping("/add")
    public ModelAndView getAddCard() {
        if (!currentUser.isLoggedIn()) {
            return super.redirect("/user/login");
        }

        if (userService.getCurrentUserCard(currentUser.getId()).isEmpty()) {
            return super.view("add-card");
        }

        return super.redirect("/home");
    }

    @GetMapping("/deposit")
    public ModelAndView postAddCard(ModelAndView mav) {
        if (!currentUser.isLoggedIn()) {
            return super.redirect("/user/login");
        }

        if (userService.getCurrentUserCard(currentUser.getId()).isEmpty()) {
            return super.redirect("add");
        } else {
            return super.redirect("cash-in");
        }
    }

    @PostMapping("/add")
    public ModelAndView postAddCard(
            @Valid @ModelAttribute(name = "cardAddModel") CardAddDto cardAddDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (!currentUser.isLoggedIn()) {
            return super.redirect("/user/login");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("cardAddModel", cardAddDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.cardAddModel", bindingResult);

            return super.redirect("add");
        }

        this.cardService.addCard(cardAddDto);

        return super.redirect("cash-in");
    }

    @GetMapping("/cash-in")
    public ModelAndView getCashIn() {
        if (!currentUser.isLoggedIn()) {
            return super.redirect("/user/login");
        }

        return super.view("add-deposit");
    }

    @PostMapping("/cash-in")
    public ModelAndView postAddCash(
            @Valid @ModelAttribute(name = "depositModel") DepositDto depositDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) throws JsonProcessingException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("depositModel", depositDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.depositModel", bindingResult);

            return super.redirect("cash-in");
        }

        this.userService.addMoney(depositDto.getAmount());

        return super.redirect("/home");
    }

    @GetMapping("/cash-out")
    public ModelAndView cashOut(ModelAndView mav) throws JsonProcessingException {
        if (!currentUser.isLoggedIn()) {
            return super.redirect("/home");
        }

        cardService.cashOut();

        return super.view("home");
    }
}