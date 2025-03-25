package app.wallet.model;

import app.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Min(value = 0)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private Currency currency;


    @ManyToOne
    private User owner;



}
