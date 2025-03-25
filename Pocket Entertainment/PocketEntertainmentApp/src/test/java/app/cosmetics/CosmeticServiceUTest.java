package app.cosmetics;

import app.cosmetic.model.BoughtCosmetic;
import app.cosmetic.model.Cosmetic;
import app.cosmetic.repository.BoughtCosmeticRepository;
import app.cosmetic.repository.CosmeticRepository;
import app.cosmetic.service.CosmeticService;
import app.exception.NoRefundsLeft;
import app.exception.NotEnoughPT;
import app.game.model.Game;
import app.game.service.GameService;
import app.user.model.User;
import app.wallet.model.Currency;
import app.wallet.model.Wallet;
import app.wallet.service.WalletService;
import app.web.dto.AddCosmeticRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CosmeticServiceUTest {

    @Mock
    private CosmeticRepository cosmeticRepository;
    @Mock
    private GameService gameService;
    @Mock
    private BoughtCosmeticRepository boughtCosmeticRepository;
    @Mock
    private WalletService walletService;

    @InjectMocks
    private CosmeticService cosmeticService;

    @Test
    void addCosmetic_callsRepository_whenCosmeticDoesNotExist() {
        AddCosmeticRequest dto = new AddCosmeticRequest();
        dto.setName("test");
        dto.setGame(new Game());

        when(cosmeticRepository.findCosmeticByGameAndName(dto.getGame(), dto.getName())).thenReturn(Optional.empty());


        cosmeticService.addCosmetic(dto);
        verify(cosmeticRepository, times(1)).findCosmeticByGameAndName(dto.getGame(), dto.getName());
        verify(cosmeticRepository, times(1)).save(any());
    }
    @Test
    void addCosmetic_throwsRuntimeException_whenCosmeticExists() {
        AddCosmeticRequest dto = new AddCosmeticRequest();
        dto.setName("test");
        dto.setGame(new Game());

        when(cosmeticRepository.findCosmeticByGameAndName(dto.getGame(), dto.getName())).thenReturn(Optional.of(new Cosmetic()));

        assertThrows(RuntimeException.class, () -> cosmeticService.addCosmetic(dto));
        verify(cosmeticRepository, times(1)).findCosmeticByGameAndName(dto.getGame(), dto.getName());
        verify(cosmeticRepository, never()).save(any());
    }

    @Test
    void getAllCosmetics_returnsListOfCosmeticsAndCallsRepository() {
        UUID id = UUID.randomUUID();
        Cosmetic cosmetic1 = new Cosmetic();
        Cosmetic cosmetic2 = new Cosmetic();

        List<Cosmetic> optCosmetics = List.of(cosmetic1, cosmetic2);

        when(cosmeticRepository.findAvailableCosmetics(id)).thenReturn(optCosmetics);

        assertEquals(optCosmetics, cosmeticService.getAllCosmetics(id));
        verify(cosmeticRepository, times(1)).findAvailableCosmetics(id);
    }
    @Test
    void getCosmeticById_callsRepository_throwsRuntimeException_whenCosmeticDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(cosmeticRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> cosmeticService.getCosmeticById(id));
        verify(cosmeticRepository, times(1)).findById(id);
    }
    @Test
    void getCosmeticById_returnsOptionalOfCosmeticAndCallsRepository() {
        UUID id = UUID.randomUUID();
        Cosmetic cosmetic1 = new Cosmetic();
        when(cosmeticRepository.findById(id)).thenReturn(Optional.of(cosmetic1));
        assertEquals(cosmetic1, cosmeticService.getCosmeticById(id));
        verify(cosmeticRepository, times(1)).findById(id);
    }
    @Test
    void buyCosmetic_doesNotIncreaseCosmeticPurchases_whenNotEnoughPTExceptionIsThrown() {
        Cosmetic cosmetic = new Cosmetic();
        cosmetic.setPurchases(0);
        cosmetic.setPrice(BigDecimal.TEN);
        User user = new User();
        Wallet ptWallet = new Wallet();
        ptWallet.setCurrency(Currency.POCKET_TOKEN);
        ptWallet.setBalance(BigDecimal.ZERO);

        when(walletService.findWalletByCurrencyAndOwner(Currency.POCKET_TOKEN, user)).thenReturn(ptWallet);

        assertThrows(NotEnoughPT.class, () -> cosmeticService.buyCosmetic(cosmetic, user));
        assertEquals(0, cosmetic.getPurchases());
        verify(walletService, times(1)).findWalletByCurrencyAndOwner(Currency.POCKET_TOKEN, user);
        verify(boughtCosmeticRepository, never()).save(any());
        verify(cosmeticRepository, never()).save(any());
    }
    @Test
    void buyCosmetic_IncreasesCosmeticPurchases_whenEnoughPT() {
        Cosmetic cosmetic = new Cosmetic();
        cosmetic.setPurchases(0);
        cosmetic.setPrice(BigDecimal.TEN);
        User user = new User();
        Wallet ptWallet = new Wallet();
        ptWallet.setCurrency(Currency.POCKET_TOKEN);
        ptWallet.setBalance(BigDecimal.valueOf(20));

        when(walletService.findWalletByCurrencyAndOwner(Currency.POCKET_TOKEN, user)).thenReturn(ptWallet);

        cosmeticService.buyCosmetic(cosmetic, user);
        assertEquals(1, cosmetic.getPurchases());
        verify(walletService, times(1)).findWalletByCurrencyAndOwner(Currency.POCKET_TOKEN, user);
        verify(boughtCosmeticRepository, times(1)).save(any());
        verify(cosmeticRepository, times(1)).save(any());
    }
    @Test
    void getAllBoughtCosmetics_returnsListOfBoughtCosmetics() {
        User user = new User();
        BoughtCosmetic cosmetic = new BoughtCosmetic();
        BoughtCosmetic cosmetic2 = new BoughtCosmetic();
        List<BoughtCosmetic> cosmetics = List.of(cosmetic, cosmetic2);

        when(boughtCosmeticRepository.findByUser(user)).thenReturn(cosmetics);

        assertEquals(cosmetics, cosmeticService.getAllBoughtCosmetics(user));
        verify(boughtCosmeticRepository, times(1)).findByUser(user);
    }
    @Test
    void refundCosmetic_throwsNoRefundsLeftExceptionAndDoesNotCallWalletServiceAndDoesNotDeleteBoughtCosmeticAndDoesNotIncreaseRefundCounter_whenRefundsAreMoreThanThree() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setRefundCount(3);
        BoughtCosmetic cosmetic = new BoughtCosmetic();
        cosmetic.setUser(user);

        when(boughtCosmeticRepository.getBoughtCosmeticById(id)).thenReturn(cosmetic);


       assertThrows(NoRefundsLeft.class, () -> cosmeticService.refundCosmetic(id));
       assertEquals(3, user.getRefundCount());
       verify(boughtCosmeticRepository, never()).deleteById(any());
       verify(walletService, never()).findWalletByCurrencyAndOwner(Currency.POCKET_TOKEN, user);
       verify(walletService, never()).saveWallet(any());
    }
    @Test
    void refundCosmetic_increaseRefundCounterAndAppendWalletBalance_whenRefundsAreLessThanThree() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setRefundCount(2);
        BoughtCosmetic cosmetic = new BoughtCosmetic();
        cosmetic.setUser(user);
        cosmetic.setPrice(BigDecimal.TEN);
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);

        when(boughtCosmeticRepository.getBoughtCosmeticById(id)).thenReturn(cosmetic);
        when(walletService.findWalletByCurrencyAndOwner(Currency.POCKET_TOKEN, user)).thenReturn(wallet);

        cosmeticService.refundCosmetic(id);
        assertEquals(3, user.getRefundCount());
        assertEquals(BigDecimal.TEN, wallet.getBalance());
        verify(boughtCosmeticRepository, times(1)).deleteById(any());
        verify(walletService, times(1)).findWalletByCurrencyAndOwner(Currency.POCKET_TOKEN, user);
        verify(walletService, times(1)).saveWallet(any());

    }

}
