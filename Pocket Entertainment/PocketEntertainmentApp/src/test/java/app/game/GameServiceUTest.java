package app.game;

import app.game.model.Game;
import app.game.repository.GameRepository;
import app.game.service.GameService;
import app.user.model.User;
import app.web.dto.AddGameRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class GameServiceUTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    void testCreateGame_whenGameNameAlreadyExists_thenThrowsRuntimeException() {
        User user = new User();
        AddGameRequest dto = new AddGameRequest();

        Game game = Game.builder()
                .name("testGame")
                .description("testDescription")
                .imageUrl("imageUrl")
                .creator(user)
                .downloads(0)
                .build();

        when(gameRepository.getGameByName(dto.getName())).thenReturn(Optional.of(game));

        assertThrows(RuntimeException.class, () -> gameService.createGame(dto, user));
        verify(gameRepository, times(1)).getGameByName(dto.getName());
    }
    @Test
    void testCreateGame_success() {

        AddGameRequest dto = new AddGameRequest();
        dto.setName("testGame");
        dto.setDescription("testDescription");
        dto.setImageUrl("imageUrl");
        User user = new User();

        when(gameRepository.getGameByName(dto.getName())).thenReturn(Optional.empty());

        gameService.createGame(dto, user);
        verify(gameRepository, times(1)).getGameByName(dto.getName());
        verify(gameRepository, times(1)).save(any(Game.class));
    }
    @Test
    void getAllGames_returnsListOfGames() {
        Game game1 = new Game();
        Game game2 = new Game();
        List<Game> testGames = List.of(game1, game2);

        when(gameRepository.findAll()).thenReturn(testGames);

        assertEquals(gameService.getAllGames(), testGames);
        verify(gameRepository, times(1)).findAll();
    }
    @Test
    void GetGameById_returnsGame_andCallsRepository() {
        UUID id = UUID.randomUUID();
        Game game = new Game();

        when(gameRepository.findById(id)).thenReturn(Optional.of(game));
        gameService.getGameById(id);

        verify(gameRepository, times(1)).findById(id);
    }
    @Test
    void getGameById_throwsRuntimeException() {
        UUID id = UUID.randomUUID();
        when(gameRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> gameService.getGameById(id));
        verify(gameRepository, times(1)).findById(id);
    }
    @Test
    void downloadGame_throwsRuntimeException_whenGameDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(gameRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> gameService.downloadGame(id));
    }
    @Test
    void downloadGame_callsGameRepositoryAndIncreasesDownloadsByOne_whenGameAlreadyExists() {
        UUID id = UUID.randomUUID();
        Game game = new Game();
        game.setDownloads(0);
        when(gameRepository.findById(id)).thenReturn(Optional.of(game));
        gameService.downloadGame(id);
        assertEquals(1, game.getDownloads());
        verify(gameRepository, times(1)).findById(id);
    }


}
