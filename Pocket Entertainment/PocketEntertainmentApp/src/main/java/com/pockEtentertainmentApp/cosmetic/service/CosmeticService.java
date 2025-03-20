package com.pockEtentertainmentApp.cosmetic.service;

import com.pockEtentertainmentApp.cosmetic.model.BoughtCosmetic;
import com.pockEtentertainmentApp.cosmetic.model.Cosmetic;
import com.pockEtentertainmentApp.cosmetic.repository.BoughtCosmeticRepository;
import com.pockEtentertainmentApp.cosmetic.repository.CosmeticRepository;
import com.pockEtentertainmentApp.exception.NoRefundsLeft;
import com.pockEtentertainmentApp.exception.NotEnoughPT;
import com.pockEtentertainmentApp.game.service.GameService;
import com.pockEtentertainmentApp.user.model.User;
import com.pockEtentertainmentApp.wallet.model.Currency;
import com.pockEtentertainmentApp.wallet.model.Wallet;
import com.pockEtentertainmentApp.wallet.repository.WalletRepository;
import com.pockEtentertainmentApp.wallet.service.WalletService;
import com.pockEtentertainmentApp.web.dto.AddCosmeticRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class CosmeticService {

    private final CosmeticRepository cosmeticRepository;
    private final GameService gameService;
    private final BoughtCosmeticRepository boughtCosmeticRepository;
    private final WalletService walletService;

    @Autowired
    public CosmeticService(CosmeticRepository cosmeticRepository, GameService gameService, BoughtCosmeticRepository boughtCosmeticRepository, WalletService walletService) {
        this.cosmeticRepository = cosmeticRepository;
        this.gameService = gameService;
        this.boughtCosmeticRepository = boughtCosmeticRepository;
        this.walletService = walletService;
    }

    public void addCosmetic(AddCosmeticRequest request) {
        Cosmetic cosmetic = Cosmetic.builder()
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .game(gameService.getGameById(request.getGame()))
                .price(request.getPrice())
                .build();

        cosmeticRepository.save(cosmetic);

    }


    public List<Cosmetic> getAllCosmetics(UUID userId) {
        return cosmeticRepository.findAvailableCosmetics(userId);
    }

    public Cosmetic getCosmeticById(UUID id) {
        return cosmeticRepository.findById(id).orElseThrow(() -> new RuntimeException("Cosmetic not found"));
    }

    @Transactional
    public void buyCosmetic(Cosmetic cosmetic, User user) {

        BoughtCosmetic boughtCosmetic = BoughtCosmetic.builder()
                .name(cosmetic.getName())
                .description(cosmetic.getDescription())
                .imageUrl(cosmetic.getImageUrl())
                .game(cosmetic.getGame())
                .price(cosmetic.getPrice())
                .cosmetic(cosmetic)
                .user(user)
                .build();

        cosmetic.setPurchases(cosmetic.getPurchases() + 1);
        cosmeticRepository.save(cosmetic);

        Wallet wallet = walletService.findWalletByCurrencyAndOwner(Currency.POCKET_TOKEN, user);

        wallet.setBalance(wallet.getBalance().subtract(cosmetic.getPrice()));
        walletService.saveWallet(wallet);
        boughtCosmeticRepository.save(boughtCosmetic);

        if (wallet.getBalance().compareTo(BigDecimal.ZERO) <= 0){
            throw new NotEnoughPT("Not enough Pocket Tokens!");
        }
    }

    public List<BoughtCosmetic> getAllBoughtCosmetics(User user) {
        return boughtCosmeticRepository.findByUser(user);
    }

    @Transactional
    public void refundCosmetic(UUID id) {
        BoughtCosmetic boughtCosmetic = boughtCosmeticRepository.getBoughtCosmeticById(id);

        User user = boughtCosmetic.getUser();

        if (user.getRefundCount() >= 3){
            throw new NoRefundsLeft("No refunds left!");
        }
        user.setRefundCount(user.getRefundCount() + 1);

        Wallet wallet = walletService.findWalletByCurrencyAndOwner(Currency.POCKET_TOKEN, user);
        wallet.setBalance(wallet.getBalance().add(boughtCosmetic.getPrice()));
        walletService.saveWallet(wallet);

        boughtCosmeticRepository.deleteById(id);
    }

}
