package com.pockEtentertainmentApp.purchases.repository;

import com.pockEtentertainmentApp.purchases.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {

}
