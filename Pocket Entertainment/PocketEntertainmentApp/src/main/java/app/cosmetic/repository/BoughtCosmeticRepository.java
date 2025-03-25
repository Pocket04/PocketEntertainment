package app.cosmetic.repository;

import app.cosmetic.model.BoughtCosmetic;
import app.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BoughtCosmeticRepository extends JpaRepository<BoughtCosmetic, UUID> {
    List<BoughtCosmetic> findByUser(User user);

    BoughtCosmetic getBoughtCosmeticById(UUID id);
}
