package com.pockEtentertainmentApp.game.service;

import com.pockEtentertainmentApp.game.model.Game;
import com.pockEtentertainmentApp.game.repository.GameRepository;
import com.pockEtentertainmentApp.web.dto.AddGameRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GameService {
    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void createGame(AddGameRequest addGameRequest) {

        Optional<Game> optional = gameRepository.getGameByName(addGameRequest.getName());

        if (optional.isPresent()) {
            throw new RuntimeException("Game with name " + addGameRequest.getName() + " already exists");
        }

        Game game = Game.builder()
                .name(addGameRequest.getName())
                .description(addGameRequest.getDescription())
                .imageUrl(addGameRequest.getImageUrl())
                .price(addGameRequest.getPrice())
                .build();

        gameRepository.save(game);
    }

    public List<Game> getAllGames() {

        return gameRepository.findAll();
    }

    public Game getGameById(UUID id) {
        return gameRepository.findById(id).orElseThrow(() -> new RuntimeException("Game with id " + id + " not found"));
    }
}
