package app.wallet.repository;

import app.user.model.User;
import app.wallet.model.Currency;
import app.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    Optional<Wallet> getWalletByCurrencyAndOwner(Currency currency, User user);
}
