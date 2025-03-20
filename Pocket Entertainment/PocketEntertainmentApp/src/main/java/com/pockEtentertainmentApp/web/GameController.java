package com.pockEtentertainmentApp.web;

import com.pockEtentertainmentApp.game.service.GameService;
import com.pockEtentertainmentApp.security.AuthenticationMetadata;
import com.pockEtentertainmentApp.user.model.User;
import com.pockEtentertainmentApp.user.service.UserService;
import com.pockEtentertainmentApp.web.dto.AddGameRequest;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @GetMapping("/{gameId}")
    public ResponseEntity<Resource> downloadGame(@PathVariable UUID gameId, HttpServletResponse response) throws MalformedURLException {
        gameService.downloadGame(gameId);

        Resource resource = new UrlResource(Paths.get("C:\\Users\\icaka\\Desktop\\Pocket Entertainment\\PocketEntertainmentApp\\src\\main\\resources\\static\\files\\virus-computer.gif").toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
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
    public String addGame(@Valid AddGameRequest addGameRequest,BindingResult bindingResult, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        if (bindingResult.hasErrors()) {
            return "add-game";
        }
        User user = userService.getUserById(authenticationMetadata.getId());

        gameService.createGame(addGameRequest, user);
        return "redirect:/home";
    }



}
