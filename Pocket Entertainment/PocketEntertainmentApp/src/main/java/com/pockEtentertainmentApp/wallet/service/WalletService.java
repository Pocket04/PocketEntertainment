package com.pockEtentertainmentApp.wallet.service;

import com.pockEtentertainmentApp.user.model.User;
import com.pockEtentertainmentApp.wallet.model.Currency;
import com.pockEtentertainmentApp.wallet.model.Wallet;
import com.pockEtentertainmentApp.wallet.repository.WalletRepository;
import com.pockEtentertainmentApp.web.dto.AddCurrencyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {


    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public void createWallet(User user, Currency currency) {
        Wallet wallet = Wallet.builder()
                .balance(BigDecimal.valueOf(20))
                .currency(currency)
                .owner(user)
                .build();
        walletRepository.save(wallet);
    }
    public Wallet findWalletById(UUID id) {
        return walletRepository.findById(id).orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    public void addCurrency(Wallet wallet, AddCurrencyRequest addCurrencyRequest) {
        wallet.setBalance(wallet.getBalance().add(addCurrencyRequest.getAmount()));
        walletRepository.save(wallet);
    }


}
