package com.pockEtentertainmentApp.web;

import com.pockEtentertainmentApp.game.model.Game;
import com.pockEtentertainmentApp.game.service.GameService;
import com.pockEtentertainmentApp.review.model.Review;
import com.pockEtentertainmentApp.review.service.ReviewService;
import com.pockEtentertainmentApp.security.AuthenticationMetadata;
import com.pockEtentertainmentApp.user.model.User;
import com.pockEtentertainmentApp.user.service.UserService;
import com.pockEtentertainmentApp.web.dto.AddReviewRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final GameService gameService;
    private final UserService userService;

    @Autowired
    public ReviewController(ReviewService reviewService, GameService gameService, UserService userService) {
        this.reviewService = reviewService;
        this.gameService = gameService;
        this.userService = userService;
    }

    @GetMapping("")
    public ModelAndView viewReviews() {

        List<Review> reviews = reviewService.findAll();
        List<Game> games = gameService.getAllGames();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("reviews");
        modelAndView.addObject("reviews", reviews);
        modelAndView.addObject("games", games);
        return modelAndView;
    }
    @GetMapping("/specific")
    public ModelAndView specificReview(@RequestParam("gameId") UUID id) {
        List<Review> reviews = reviewService.findByGame(id);
        List<Game> games = gameService.getAllGames();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("specificReviews");
        modelAndView.addObject("reviews", reviews);
        modelAndView.addObject("games", games);

        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView addReview() {

        List<Game> games = gameService.getAllGames();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add-review");
        modelAndView.addObject("addReviewRequest", new AddReviewRequest());
        modelAndView.addObject("games", games);
        return modelAndView;
    }
    @PostMapping("/add")
    public ModelAndView completeAddReview(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata, @Valid AddReviewRequest addReviewRequest, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("add-review");
            List<Game> games = gameService.getAllGames();
            modelAndView.addObject("games", games);
            return modelAndView;
        }

        User user = userService.getUserById(authenticationMetadata.getId());

        reviewService.addReview(addReviewRequest, user);

        return new ModelAndView("redirect:/reviews");
    }

}
