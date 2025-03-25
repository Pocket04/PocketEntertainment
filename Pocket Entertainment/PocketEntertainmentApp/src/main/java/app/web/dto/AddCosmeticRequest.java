package app.web.dto;

import app.game.model.Game;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AddCosmeticRequest {

    @NotBlank(message = "Please enter name.")
    private String name;
    @NotBlank(message = "Must be a valid URL!")
    @URL
    private String imageUrl;
    @NotBlank(message = "Please enter description.")
    @Size(min = 1, max = 50, message = "Must be between 1 and 50 symbols!")
    private String description;
    @NotNull(message = "Please select a game!")
    private Game game;
    @NotNull(message = "Please enter the price.")
    private BigDecimal price;


}
