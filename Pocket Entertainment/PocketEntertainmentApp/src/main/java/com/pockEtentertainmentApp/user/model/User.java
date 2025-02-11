package com.pockEtentertainmentApp.user.model;

import com.pockEtentertainmentApp.cosmetic.model.Cosmetic;
import com.pockEtentertainmentApp.game.model.Game;
import com.pockEtentertainmentApp.wallet.model.Wallet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @URL
    private String profilePicture;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Game> games;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Wallet> wallets;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Cosmetic> cosmetics;



}
