package com.pockEtentertainmentApp.cosmetic.model;

import com.pockEtentertainmentApp.game.model.Game;
import com.pockEtentertainmentApp.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BoughtCosmetic {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @URL
    private String imageUrl;

    @Column(nullable = false)
    @Size(max = 1000)
    private String description;

    @ManyToOne
    private Game game;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    private Cosmetic cosmetic;

    @ManyToOne
    private User user;



}
