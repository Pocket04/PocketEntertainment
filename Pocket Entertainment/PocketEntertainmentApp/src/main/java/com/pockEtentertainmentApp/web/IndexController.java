package com.pockEtentertainmentApp.web;

import com.pockEtentertainmentApp.cosmetic.model.BoughtCosmetic;
import com.pockEtentertainmentApp.cosmetic.model.Cosmetic;
import com.pockEtentertainmentApp.cosmetic.service.CosmeticService;
import com.pockEtentertainmentApp.game.model.Game;
import com.pockEtentertainmentApp.game.service.GameService;
import com.pockEtentertainmentApp.notification.dto.NotificationRequest;
import com.pockEtentertainmentApp.notification.service.NotificationService;
import com.pockEtentertainmentApp.security.AuthenticationMetadata;
import com.pockEtentertainmentApp.user.model.User;
import com.pockEtentertainmentApp.user.service.UserService;
import com.pockEtentertainmentApp.wallet.model.Wallet;
import com.pockEtentertainmentApp.web.dto.LoginRequest;
import com.pockEtentertainmentApp.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class IndexController {

    private final UserService userService;
    private final GameService gameService;
    private final CosmeticService cosmeticService;
    private final NotificationService notificationService;

    @Autowired
    public IndexController(UserService userService, GameService gameService, CosmeticService cosmeticService, NotificationService notificationService) {
        this.userService = userService;
        this.gameService = gameService;
        this.cosmeticService = cosmeticService;
        this.notificationService = notificationService;
    }


    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/home")
    public ModelAndView homePage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getUserById(authenticationMetadata.getId());
        List<Game> games = gameService.getAllGames();
        List<Cosmetic> cosmetics = cosmeticService.getAllCosmetics(authenticationMetadata.getId());
        List<BoughtCosmetic> boughtCosmetics = cosmeticService.getAllBoughtCosmetics(user);
        List<Wallet> wallets = user.getWallets();


        ModelAndView mav = new ModelAndView();
        mav.setViewName("home");
        mav.addObject("user", user);
        mav.addObject("games", games);
        mav.addObject("cosmetics", cosmetics);
        mav.addObject("boughtCosmetics", boughtCosmetics);
        mav.addObject("wallets", wallets);
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

    @GetMapping("/planet-of-peace")
    public ModelAndView planetOfPeace(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("POP");
        return modelAndView;
    }

    @GetMapping("/contacts")
    public ModelAndView contacts() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("contacts");
        modelAndView.addObject("notificationRequest", new NotificationRequest());
        return modelAndView;
    }

    @PostMapping("/contacts")
    public String sendEmail(@Valid NotificationRequest notificationRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        if (bindingResult.hasErrors()) {
            return "contacts";
        }
        notificationService.sendNotification(notificationRequest, authenticationMetadata.getId());

        return "redirect:/home";
    }

}
