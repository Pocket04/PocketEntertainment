package com.pockEtentertainmentApp.wallet.repository;

import com.pockEtentertainmentApp.user.model.User;
import com.pockEtentertainmentApp.wallet.model.Currency;
import com.pockEtentertainmentApp.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    Wallet getWalletByCurrencyAndOwner(Currency currency, User user);
}
