package com.pockEtentertainmentApp.game.service;

import com.pockEtentertainmentApp.game.model.Game;
import com.pockEtentertainmentApp.game.repository.GameRepository;
import com.pockEtentertainmentApp.review.model.Review;
import com.pockEtentertainmentApp.user.model.User;
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


    public void createGame(AddGameRequest addGameRequest, User user) {

        Optional<Game> optional = gameRepository.getGameByName(addGameRequest.getName());

        if (optional.isPresent()) {
            throw new RuntimeException("Game with name " + addGameRequest.getName() + " already exists");
        }

        Game game = Game.builder()
                .name(addGameRequest.getName())
                .description(addGameRequest.getDescription())
                .imageUrl(addGameRequest.getImageUrl())
                .creator(user)
                .downloads(0)
                .build();

        gameRepository.save(game);
    }

    public List<Game> getAllGames() {

        return gameRepository.findAll();
    }
    public void downloadGame(UUID gameId) {
        Optional<Game> optional = gameRepository.getGameById(gameId);
        if (optional.isEmpty()) {
            throw new RuntimeException("Game with id " + gameId + " does not exist");
        }

        Game game = optional.get();
        int downloads = game.getDownloads() + 1;
        game.setDownloads(downloads);
        gameRepository.save(game);
    }

    public Game getGameById(UUID id) {
        return gameRepository.findById(id).orElseThrow(() -> new RuntimeException("Game with id " + id + " not found"));
    }
}
