package app.game.model;

import app.cosmetic.model.Cosmetic;
import app.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Table
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique=true, nullable = false)
    private String name;

    @Column(nullable = false)
    @Size(max=1000)
    private String description;

    @Column(nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "game")
    private List<Cosmetic> cosmetics;

    @ManyToOne
    private User creator;

    @Column(nullable = false)
    private long downloads;

}
