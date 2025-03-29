package app;


import app.cosmetic.model.Cosmetic;
import app.cosmetic.service.CosmeticService;
import app.game.model.Game;
import app.game.service.GameService;
import app.user.model.Role;
import app.user.model.User;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.wallet.model.Currency;
import app.wallet.repository.WalletRepository;
import app.wallet.service.WalletService;
import app.web.dto.AddCosmeticRequest;
import app.web.dto.AddGameRequest;
import app.web.dto.RegisterRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class BuyCosmeticITest {

    @Autowired
    private CosmeticService cosmeticService;
    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;


    @Test
    void buyCosmetic_happyPath() {
        RegisterRequest regDto = RegisterRequest.builder()
                .username("test")
                .email("test@example.com")
                .password("password")
                .build();
        userService.registerUser(regDto);
        Optional<User> user1 = userRepository.findByUsername("test");
        if (user1.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = user1.get();

        AddGameRequest addGameRequest = new AddGameRequest();
        addGameRequest.setDescription("test description");
        addGameRequest.setName("test name");
        addGameRequest.setImageUrl("https://www.google.com");

        gameService.createGame(addGameRequest, user);
        Game game = gameService.getAllGames().get(0);

        AddCosmeticRequest dto = new AddCosmeticRequest();
        dto.setGame(game);
        dto.setName("test cosmetic");
        dto.setPrice(BigDecimal.valueOf(10));
        dto.setDescription("test description");
        dto.setImageUrl("https://www.google.com");

        cosmeticService.addCosmetic(dto);

        Cosmetic cosmetic = cosmeticService.getAllCosmetics(user.getId()).get(0);

        cosmeticService.buyCosmetic(cosmetic, user);

        assertEquals(2, user.getWallets().size());
        assertFalse(cosmeticService.getAllBoughtCosmetics(user).isEmpty());
        assertEquals(1, cosmetic.getPurchases());
    }

}
