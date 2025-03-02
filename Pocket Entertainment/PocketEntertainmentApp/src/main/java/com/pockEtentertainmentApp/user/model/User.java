package com.pockEtentertainmentApp.user.model;

import com.pockEtentertainmentApp.cosmetic.model.Cosmetic;
import com.pockEtentertainmentApp.game.model.Game;
import com.pockEtentertainmentApp.review.model.Review;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String profilePicture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private boolean isActive;

}
