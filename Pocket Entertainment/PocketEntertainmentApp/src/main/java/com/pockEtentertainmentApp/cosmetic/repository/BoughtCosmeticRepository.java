package com.pockEtentertainmentApp.cosmetic.repository;

import com.pockEtentertainmentApp.cosmetic.model.BoughtCosmetic;
import com.pockEtentertainmentApp.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BoughtCosmeticRepository extends JpaRepository<BoughtCosmetic, UUID> {
    List<BoughtCosmetic> findByUser(User user);

    BoughtCosmetic getBoughtCosmeticById(UUID id);
}
