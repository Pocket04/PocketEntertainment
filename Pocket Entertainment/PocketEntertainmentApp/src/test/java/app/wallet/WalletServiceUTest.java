package app.wallet;

import app.user.model.User;
import app.wallet.model.Currency;
import app.wallet.model.Wallet;
import app.wallet.repository.WalletRepository;
import app.wallet.service.WalletService;
import app.web.dto.AddCurrencyRequest;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceUTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    private User user;
    private Currency currency;
    private Wallet ptWallet;
    private Wallet eurWallet;

    @BeforeEach
    void setUp() {
        ptWallet = new Wallet(UUID.randomUUID(), BigDecimal.valueOf(20), Currency.POCKET_TOKEN, user);
        eurWallet = new Wallet(UUID.randomUUID(), BigDecimal.valueOf(20), Currency.EURO, user);
        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");
        user.setWallets(List.of(ptWallet, eurWallet));

        currency = Currency.POCKET_TOKEN;
    }


    @Test
    void findWalletById_whenWalletExists_returnWallet() {

        UUID id = UUID.randomUUID();
        Wallet wallet = new Wallet();

        when(walletRepository.findById(id)).thenReturn(Optional.of(wallet));

        assertEquals(walletService.findWalletById(id), wallet);
    }

    @Test
    void findWalletById_whenWalletDoesntExist_returnException() {

        when(walletRepository.findById(UUID.randomUUID())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> walletService.findWalletById(UUID.randomUUID()));
    }

    @Test
    void createWallet_savedWallet_isSameAsGivenWallet() {
        walletService.createWallet(user, currency);

        verify(walletRepository, times(1)).save(any(Wallet.class));
    }
    @Test
    void findWalletByCurrencyAndOwner_whenWalletExists_returnWallet() {
        Wallet wallet = Wallet.builder()
                .owner(user)
                .currency(currency)
                .build();

        when(walletRepository.getWalletByCurrencyAndOwner(currency, user)).thenReturn(Optional.of(wallet));

        assertEquals(walletService.findWalletByCurrencyAndOwner(currency, user), wallet);
    }
    @Test
    void findWalletByCurrencyAndOwner_whenWalletDoesntExist_returnException() {
        when(walletRepository.getWalletByCurrencyAndOwner(currency, user)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> walletService.findWalletByCurrencyAndOwner(currency, user));
    }
    @Test
    void updateEURWallet_success() {
        AddCurrencyRequest dto = new AddCurrencyRequest();
        dto.setAmount(BigDecimal.TEN);

        walletService.addCurrency(eurWallet, dto, user);

        assertEquals(eurWallet.getBalance(), BigDecimal.valueOf(30));
    }
    @Test
    void updatePTWallet_success() {
        AddCurrencyRequest dto = new AddCurrencyRequest();
        dto.setAmount(BigDecimal.valueOf(20));

        when(walletRepository.getWalletByCurrencyAndOwner(Currency.EURO, user)).thenReturn(Optional.of(eurWallet));

        walletService.addCurrency(ptWallet, dto, user);
        assertEquals(ptWallet.getBalance(), BigDecimal.valueOf(40));
        assertEquals(eurWallet.getBalance(), BigDecimal.valueOf(10.0));
    }
    @Test
    void updatePTWallet_WhenEuroWalletHasNotEnoughBalance_returnException() {
        AddCurrencyRequest dto = new AddCurrencyRequest();
        dto.setAmount(BigDecimal.valueOf(50));

        when(walletRepository.getWalletByCurrencyAndOwner(Currency.EURO, user)).thenReturn(Optional.of(eurWallet));

        assertThrows(RuntimeException.class, () -> walletService.addCurrency(ptWallet, dto, user));
    }
    @Test
    void refundMoney_whenWalletExists_returnWallet() {

        when(walletRepository.getWalletByCurrencyAndOwner(currency, user)).thenReturn(Optional.of(ptWallet));

        walletService.refundPT(BigDecimal.TEN, user);
        verify(walletRepository, times(1)).save(any(Wallet.class));
        assertEquals(ptWallet.getBalance(), BigDecimal.valueOf(30));


    }

}
