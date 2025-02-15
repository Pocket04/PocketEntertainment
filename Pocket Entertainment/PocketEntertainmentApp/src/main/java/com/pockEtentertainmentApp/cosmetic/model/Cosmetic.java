package com.pockEtentertainmentApp.cosmetic.model;

import com.pockEtentertainmentApp.game.model.Game;
import com.pockEtentertainmentApp.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Cosmetic {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @URL
    private String imageUrl;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    private Game game;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    private User owner;
}
