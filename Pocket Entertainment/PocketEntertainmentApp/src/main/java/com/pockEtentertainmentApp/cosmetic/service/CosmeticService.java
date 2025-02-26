package com.pockEtentertainmentApp.cosmetic.service;

import com.pockEtentertainmentApp.cosmetic.model.Cosmetic;
import com.pockEtentertainmentApp.cosmetic.repository.CosmeticRepository;
import com.pockEtentertainmentApp.game.service.GameService;
import com.pockEtentertainmentApp.web.dto.AddCosmeticRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CosmeticService {

    private final CosmeticRepository cosmeticRepository;
    private final GameService gameService;

    @Autowired
    public CosmeticService(CosmeticRepository cosmeticRepository, GameService gameService) {
        this.cosmeticRepository = cosmeticRepository;
        this.gameService = gameService;
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


    public List<Cosmetic> getallCosmtics() {
        return cosmeticRepository.findAll();
    }
}
