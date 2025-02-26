package com.pockEtentertainmentApp.web;

import com.pockEtentertainmentApp.cosmetic.model.Cosmetic;
import com.pockEtentertainmentApp.cosmetic.service.CosmeticService;
import com.pockEtentertainmentApp.game.model.Game;
import com.pockEtentertainmentApp.game.service.GameService;
import com.pockEtentertainmentApp.security.AuthenticationMetadata;
import com.pockEtentertainmentApp.user.model.User;
import com.pockEtentertainmentApp.user.service.UserService;
import com.pockEtentertainmentApp.web.dto.AddGameRequest;
import com.pockEtentertainmentApp.web.dto.LoginRequest;
import com.pockEtentertainmentApp.web.dto.RegisterRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class IndexController {

    private final UserService userService;
    private final GameService gameService;
    private final CosmeticService cosmeticService;

    @Autowired
    public IndexController(UserService userService, GameService gameService, CosmeticService cosmeticService) {
        this.userService = userService;
        this.gameService = gameService;
        this.cosmeticService = cosmeticService;
    }


    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/home")
    public ModelAndView homePage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getUserById(authenticationMetadata.getId());
        List<Game> games = gameService.getAllGames();
        List<Cosmetic> cosmetics = cosmeticService.getallCosmtics();


        ModelAndView mav = new ModelAndView();
        mav.setViewName("home");
        mav.addObject("user", user);
        mav.addObject("games", games);
        mav.addObject("cosmetics", cosmetics);
        return mav;
    }
    @GetMapping("/login")
    public ModelAndView loginPage(@RequestParam(value = "error", required = false) String errorParam) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", new LoginRequest());

        if (errorParam != null) {
            modelAndView.addObject("errorMessage", "Incorrect username or password.");
        }

        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView registerPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        modelAndView.addObject("registerRequest", new RegisterRequest());

        return modelAndView;


    }

    @PostMapping("/register")
    public String registerPage(@Valid RegisterRequest registerRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.registerUser(registerRequest);

        return "redirect:/login";
    }
    @GetMapping("/add-game")
    public ModelAndView addGame(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add-game");
        modelAndView.addObject("addGameRequest", new AddGameRequest());

        return modelAndView;
    }
    @PostMapping("/add-game")
    public String addGame(@Valid AddGameRequest addGameRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add-game";
        }
        gameService.createGame(addGameRequest);
        return "redirect:/home";
    }
    @GetMapping("/planet-of-peace")
    public ModelAndView planetOfPeace(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("POP");
        return modelAndView;
    }

}
