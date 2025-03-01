package com.pockEtentertainmentApp.web;

import com.pockEtentertainmentApp.game.service.GameService;
import com.pockEtentertainmentApp.security.AuthenticationMetadata;
import com.pockEtentertainmentApp.user.model.User;
import com.pockEtentertainmentApp.user.service.UserService;
import com.pockEtentertainmentApp.web.dto.AddGameRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;
    private final UserService userService;

    @Autowired
    public GameController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @PostMapping("/{gameId}")
    public String downloadGame(@PathVariable UUID gameId){

        gameService.downloadGame(gameId);

        return "redirect:/home";
    }

    @GetMapping("/add-game")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addGame() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add-game");
        modelAndView.addObject("addGameRequest", new AddGameRequest());

        return modelAndView;
    }

    @PostMapping("/add-game")
    @PreAuthorize("hasRole('ADMIN')")
    public String addGame(@Valid AddGameRequest addGameRequest, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add-game";
        }
        User user = userService.getUserById(authenticationMetadata.getId());

        gameService.createGame(addGameRequest, user);
        return "redirect:/home";
    }



}
