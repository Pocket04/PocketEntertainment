package app.cosmetic.model;

import app.game.model.Game;
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
@Getter
@Setter
public class Cosmetic {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    @URL
    private String imageUrl;

    @Column(nullable = false)
    @Size(max=1000)
    private String description;

    @ManyToOne
    private Game game;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private long purchases;
}
