package app.cosmetic.repository;

import app.cosmetic.model.Cosmetic;
import app.game.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CosmeticRepository extends JpaRepository<Cosmetic, UUID> {

    @Query("SELECT c FROM Cosmetic c WHERE c.id NOT IN " +
            "(SELECT bc.cosmetic.id FROM BoughtCosmetic bc WHERE bc.user.id = :userId)")
    List<Cosmetic> findAvailableCosmetics(@Param("userId") UUID userId);
    Optional<Cosmetic> findCosmeticByGameAndName(Game game, String name);

}
