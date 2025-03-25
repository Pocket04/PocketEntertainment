package app.wallet.service;

import app.exception.NoEuroException;
import app.user.model.User;
import app.wallet.model.Currency;
import app.wallet.model.Wallet;
import app.wallet.repository.WalletRepository;
import app.web.dto.AddCurrencyRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {


    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public void createWallet(User user, Currency currency) {
        Wallet wallet = Wallet.builder()
                .balance(BigDecimal.valueOf(20))
                .currency(currency)
                .owner(user)
                .build();
        walletRepository.save(wallet);
    }

    public Wallet findWalletById(UUID id) {
        return walletRepository.findById(id).orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    public Wallet findWalletByCurrencyAndOwner(Currency currency, User user) {
        return walletRepository.getWalletByCurrencyAndOwner(currency, user).orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    public void saveWallet(Wallet wallet) {
        walletRepository.save(wallet);
    }

    @Transactional
    public void addCurrency(Wallet ptWallet, AddCurrencyRequest addCurrencyRequest, User user) {

        if (ptWallet.getCurrency() == Currency.EURO) {
            ptWallet.setBalance(ptWallet.getBalance().add(addCurrencyRequest.getAmount()));
            walletRepository.save(ptWallet);
            return;
        }

        Wallet eurWallet = findWalletByCurrencyAndOwner(Currency.EURO, user);

        BigDecimal eurBalance = eurWallet.getBalance();
        BigDecimal ptBalance = ptWallet.getBalance();

        eurBalance = eurBalance.subtract(addCurrencyRequest.getAmount().multiply(BigDecimal.valueOf(0.5)));
        ptBalance = ptBalance.add(addCurrencyRequest.getAmount());

        if(eurBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NoEuroException("EUR Balance less than 0");
        }
        eurWallet.setBalance(eurBalance);
        ptWallet.setBalance(ptBalance);
        walletRepository.save(eurWallet);
        walletRepository.save(ptWallet);
    }



}
