package com.pockEtentertainmentApp.cosmetic.repository;

import com.pockEtentertainmentApp.cosmetic.model.Cosmetic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CosmeticRepository extends JpaRepository<Cosmetic, UUID> {
}
