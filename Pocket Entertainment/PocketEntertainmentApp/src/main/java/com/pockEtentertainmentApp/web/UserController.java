package com.pockEtentertainmentApp.web;

import com.pockEtentertainmentApp.user.model.User;
import com.pockEtentertainmentApp.user.service.UserService;
import com.pockEtentertainmentApp.web.dto.EditAccountRequest;
import com.pockEtentertainmentApp.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView users() {
        List<User> users = userService.getAllUsers();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users");
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView account(@PathVariable UUID id) {
        User user = userService.getUserById(id);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("account");
        mav.addObject("user", user);
        return mav;
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public String makeUserAdmin(@PathVariable UUID id) {

        User user = userService.getUserById(id);
        userService.makeAdmin(user);
        return "redirect:/users";
    }

    @GetMapping("/{id}/profile")
    public ModelAndView profile(@PathVariable UUID id) {
        User user = userService.getUserById(id);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("edit-profile");
        mav.addObject("user", user);
        mav.addObject("editAccountRequest", DtoMapper.mapToEditAccountRequest(user));
        return mav;
    }

    @PutMapping("/{id}/profile")
    public ModelAndView editProfile(@PathVariable UUID id, @Valid EditAccountRequest editAccountRequest, BindingResult bindingResult) {
        User user = userService.getUserById(id);

        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("edit-profile");
            mav.addObject("user", user);
            mav.addObject("editAccountRequest", DtoMapper.mapToEditAccountRequest(user));
            return mav;
        }

        userService.editUser(user, editAccountRequest);

        return new ModelAndView("redirect:/users/" + id);
    }
}
