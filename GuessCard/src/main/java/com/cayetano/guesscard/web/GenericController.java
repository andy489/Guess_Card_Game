package com.cayetano.guesscard.web;

import org.springframework.web.servlet.ModelAndView;

public class GenericController {
    private static final String REDIRECT = "redirect:";

    public ModelAndView view(String viewName, ModelAndView modelAndView) {
        modelAndView.setViewName(viewName);

        return modelAndView;
    }

    public ModelAndView view(String viewName) {
        return this.view(viewName, new ModelAndView());
    }

    public ModelAndView redirect(String url) {
        return this.view(REDIRECT + url);
    }
}
