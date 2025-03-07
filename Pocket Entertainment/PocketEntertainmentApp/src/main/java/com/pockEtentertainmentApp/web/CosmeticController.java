package com.pockEtentertainmentApp.web;

import com.pockEtentertainmentApp.cosmetic.model.Cosmetic;
import com.pockEtentertainmentApp.cosmetic.service.CosmeticService;
import com.pockEtentertainmentApp.game.model.Game;
import com.pockEtentertainmentApp.game.service.GameService;
import com.pockEtentertainmentApp.security.AuthenticationMetadata;
import com.pockEtentertainmentApp.user.model.User;
import com.pockEtentertainmentApp.user.service.UserService;
import com.pockEtentertainmentApp.web.dto.AddCosmeticRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cosmetics")
public class CosmeticController {
    private final GameService gameService;
    private final CosmeticService cosmeticService;
    private final UserService userService;

    @Autowired
    public CosmeticController(GameService gameService, CosmeticService cosmeticService, UserService userService) {
        this.gameService = gameService;
        this.cosmeticService = cosmeticService;
        this.userService = userService;
    }


    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addCosmeticForm() {

        List<Game> games = gameService.getAllGames();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add-cosmetic");
        modelAndView.addObject("addCosmeticRequest", new AddCosmeticRequest());
        modelAndView.addObject("games", games);
        return modelAndView;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addCosmetic(@Valid AddCosmeticRequest addCosmeticRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<Game> games = gameService.getAllGames();
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("add-cosmetic");
            modelAndView.addObject("games", games);
            return modelAndView;
        }

        cosmeticService.addCosmetic(addCosmeticRequest);

        return new ModelAndView("redirect:/home");
    }

    @PostMapping("/{id}/bought")
    public String buyCosmetic(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetadata metadata) {

        User user = userService.getUserById(metadata.getId());

        Cosmetic cosmetic = cosmeticService.getCosmeticById(id);
        cosmeticService.createBoughtCosmetic(cosmetic, user);

        return "redirect:/home";
    }
    @DeleteMapping("/{id}/bought")
    public String deleteCosmetic(@PathVariable UUID id) {
        cosmeticService.deleteBoughtCosmetic(id);
        return "redirect:/home";
    }

}
