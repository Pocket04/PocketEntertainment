package com.pockEtentertainmentApp.web;

import com.pockEtentertainmentApp.cosmetic.model.Cosmetic;
import com.pockEtentertainmentApp.cosmetic.service.CosmeticService;
import com.pockEtentertainmentApp.game.model.Game;
import com.pockEtentertainmentApp.game.service.GameService;
import com.pockEtentertainmentApp.web.dto.AddCosmeticRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/cosmetics")
public class CosmeticController {
    private final GameService gameService;
    private final CosmeticService cosmeticService;

    @Autowired
    public CosmeticController(GameService gameService, CosmeticService cosmeticService) {
        this.gameService = gameService;
        this.cosmeticService = cosmeticService;
    }


    @GetMapping("/add")
    public ModelAndView addCosmeticForm() {

        List<Game> games = gameService.getAllGames();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add-cosmetic");
        modelAndView.addObject("addCosmeticRequest", new AddCosmeticRequest());
        modelAndView.addObject("games", games);
        return modelAndView;
    }

    @PostMapping("/add")
    public String addCosmetic(@Valid AddCosmeticRequest addCosmeticRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "add-cosmetic";
        }

        cosmeticService.addCosmetic(addCosmeticRequest);

        return "redirect:/home";
    }
}
