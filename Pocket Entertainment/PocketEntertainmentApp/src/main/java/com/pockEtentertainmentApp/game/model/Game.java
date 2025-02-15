package com.pockEtentertainmentApp.game.model;

import com.pockEtentertainmentApp.cosmetic.model.Cosmetic;
import com.pockEtentertainmentApp.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Table
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique=true)
    private String name;

    @Column
    private String description;

    @Column
    private BigDecimal price;

    @OneToMany(mappedBy = "game")
    private List<Cosmetic> cosmetics;

    @ManyToOne
    private User owner;

}
