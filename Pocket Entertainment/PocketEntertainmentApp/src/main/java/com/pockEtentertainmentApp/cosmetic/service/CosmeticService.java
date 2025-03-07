package com.pockEtentertainmentApp.cosmetic.service;

import com.pockEtentertainmentApp.cosmetic.model.BoughtCosmetic;
import com.pockEtentertainmentApp.cosmetic.model.Cosmetic;
import com.pockEtentertainmentApp.cosmetic.repository.BoughtCosmeticRepository;
import com.pockEtentertainmentApp.cosmetic.repository.CosmeticRepository;
import com.pockEtentertainmentApp.game.service.GameService;
import com.pockEtentertainmentApp.user.model.User;
import com.pockEtentertainmentApp.web.dto.AddCosmeticRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CosmeticService {

    private final CosmeticRepository cosmeticRepository;
    private final GameService gameService;
    private final BoughtCosmeticRepository boughtCosmeticRepository;

    @Autowired
    public CosmeticService(CosmeticRepository cosmeticRepository, GameService gameService, BoughtCosmeticRepository boughtCosmeticRepository) {
        this.cosmeticRepository = cosmeticRepository;
        this.gameService = gameService;
        this.boughtCosmeticRepository = boughtCosmeticRepository;
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

    public void createBoughtCosmetic(Cosmetic cosmetic, User user) {

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

    }

    public List<BoughtCosmetic> getAllBoughtCosmetics(User user) {
        return boughtCosmeticRepository.findByUser(user);
    }
    public void deleteBoughtCosmetic(UUID id) {
        boughtCosmeticRepository.deleteById(id);
    }

}
