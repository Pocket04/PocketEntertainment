package app.game.service;

import app.game.model.Game;
import app.game.repository.GameRepository;
import app.user.model.User;
import app.web.dto.AddGameRequest;
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
        Optional<Game> optional = gameRepository.findById(gameId);
        if (optional.isEmpty()) {
            throw new RuntimeException("Game with id " + gameId + " does not exist");
        }

        Game game = optional.get();
        long downloads = game.getDownloads() + 1;
        game.setDownloads(downloads);
        gameRepository.save(game);
    }

    public Game getGameById(UUID id) {
        return gameRepository.findById(id).orElseThrow(() -> new RuntimeException("Game with id " + id + " not found"));
    }
}
