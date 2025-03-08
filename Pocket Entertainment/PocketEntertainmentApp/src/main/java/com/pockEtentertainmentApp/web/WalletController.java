package com.pockEtentertainmentApp.web;

import com.pockEtentertainmentApp.security.AuthenticationMetadata;
import com.pockEtentertainmentApp.user.model.User;
import com.pockEtentertainmentApp.user.service.UserService;
import com.pockEtentertainmentApp.wallet.model.Wallet;
import com.pockEtentertainmentApp.wallet.service.WalletService;
import com.pockEtentertainmentApp.web.dto.AddCurrencyRequest;
import jakarta.validation.Valid;
import org.hibernate.boot.Metadata;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/wallets")
public class WalletController {


    private final WalletService walletService;
    private final UserService userService;

    public WalletController(WalletService walletService, UserService userService) {
        this.walletService = walletService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ModelAndView buy(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetadata metadata) {

        User user = userService.getUserById(id);
        List<Wallet> wallets = userService.getUserById(metadata.getId()).getWallets();

        ModelAndView mav = new ModelAndView();
        mav.setViewName("wallets");
        mav.addObject("wallets", wallets);
        mav.addObject("addCurrencyRequest", new AddCurrencyRequest());
        mav.addObject("user", user);
        return mav;
    }
    @PutMapping("/{id}")
    public String addCurrency(@PathVariable UUID id,@AuthenticationPrincipal AuthenticationMetadata metadata, @Valid AddCurrencyRequest addCurrencyRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "redirect:/home";
        }
        User user = userService.getUserById(metadata.getId());
        Wallet wallet = walletService.findWalletById(id);
        walletService.addCurrency(wallet, addCurrencyRequest, user);

        return "redirect:/home";
    }

}
