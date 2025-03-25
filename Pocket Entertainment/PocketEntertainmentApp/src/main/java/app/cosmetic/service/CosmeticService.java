package app.cosmetic.service;

import app.cosmetic.model.BoughtCosmetic;
import app.cosmetic.model.Cosmetic;
import app.cosmetic.repository.BoughtCosmeticRepository;
import app.cosmetic.repository.CosmeticRepository;
import app.exception.NoRefundsLeft;
import app.exception.NotEnoughPT;
import app.game.service.GameService;
import app.user.model.User;
import app.wallet.model.Currency;
import app.wallet.model.Wallet;
import app.wallet.service.WalletService;
import app.web.dto.AddCosmeticRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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
        Optional<Cosmetic> optCosmetic = cosmeticRepository.findCosmeticByGameAndName(request.getGame(), request.getName());
        if (optCosmetic.isPresent()) {
            throw new RuntimeException("Cosmetic already exists");
        }
        Cosmetic cosmetic = Cosmetic.builder()
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .game(request.getGame())
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
        Wallet wallet = walletService.findWalletByCurrencyAndOwner(Currency.POCKET_TOKEN, user);

        wallet.setBalance(wallet.getBalance().subtract(cosmetic.getPrice()));
        walletService.saveWallet(wallet);

        if (wallet.getBalance().compareTo(BigDecimal.ZERO) <= 0){
            throw new NotEnoughPT("Not enough Pocket Tokens!");
        }

        BoughtCosmetic boughtCosmetic = BoughtCosmetic.builder()
                .name(cosmetic.getName())
                .description(cosmetic.getDescription())
                .imageUrl(cosmetic.getImageUrl())
                .game(cosmetic.getGame())
                .price(cosmetic.getPrice())
                .cosmetic(cosmetic)
                .user(user)
                .build();

        boughtCosmeticRepository.save(boughtCosmetic);
        cosmetic.setPurchases(cosmetic.getPurchases() + 1);
        cosmeticRepository.save(cosmetic);

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
