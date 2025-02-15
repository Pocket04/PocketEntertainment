package com.pockEtentertainmentApp.web;

import com.pockEtentertainmentApp.user.model.User;
import com.pockEtentertainmentApp.user.service.UserService;
import com.pockEtentertainmentApp.web.dto.LoginRequest;
import com.pockEtentertainmentApp.web.dto.RegisterRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class IndexController {

    private final UserService userService;

    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/home")
    public ModelAndView homePage(HttpSession session) {

        UUID uuid = (UUID) session.getAttribute("user_id");
        User user = userService.getUserById(uuid);

        if (user == null) {
            return new ModelAndView("redirect:/login");
        }


        ModelAndView mav = new ModelAndView();
        mav.setViewName("home");
        mav.addObject("user", user);
        return mav;
    }
    @GetMapping("/login")
    public ModelAndView loginPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", new LoginRequest());

        return modelAndView;
    }
    @PostMapping("/login")
    public String login(@Valid LoginRequest loginRequest, BindingResult bindingResult, HttpSession httpSession){

        if (bindingResult.hasErrors()) {
            return "login";
        }
        User user = userService.login(loginRequest);
        httpSession.setAttribute("user_id", user.getId());

        return "redirect:/home";
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

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

}
