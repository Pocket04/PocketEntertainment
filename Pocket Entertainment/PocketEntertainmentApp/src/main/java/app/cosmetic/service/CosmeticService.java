package app.cosmetic.service;

import app.cosmetic.model.BoughtCosmetic;
import app.cosmetic.model.Cosmetic;
import app.cosmetic.repository.BoughtCosmeticRepository;
import app.cosmetic.repository.CosmeticRepository;
import app.exception.NoRefundsLeft;
import app.user.model.User;
import app.wallet.service.WalletService;
import app.web.dto.AddCosmeticRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CosmeticService {

    private final CosmeticRepository cosmeticRepository;
    private final BoughtCosmeticRepository boughtCosmeticRepository;
    private final WalletService walletService;

    @Autowired
    public CosmeticService(CosmeticRepository cosmeticRepository, BoughtCosmeticRepository boughtCosmeticRepository, WalletService walletService) {
        this.cosmeticRepository = cosmeticRepository;
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

        walletService.spendMoney(cosmetic.getPrice(), user);

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

        walletService.refundPT(boughtCosmetic.getPrice(), user);

        boughtCosmeticRepository.deleteById(id);
    }

}
