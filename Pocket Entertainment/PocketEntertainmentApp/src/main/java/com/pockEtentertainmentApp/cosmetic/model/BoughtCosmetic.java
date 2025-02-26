package com.pockEtentertainmentApp.cosmetic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class BoughtCosmetic {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;



}
