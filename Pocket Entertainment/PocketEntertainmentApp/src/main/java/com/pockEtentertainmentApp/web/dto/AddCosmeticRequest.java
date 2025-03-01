package com.pockEtentertainmentApp.web.dto;

import com.pockEtentertainmentApp.game.model.Game;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String description;
    @NotNull(message = "Please select a game!")
    private UUID game;
    @NotNull(message = "Please enter the price.")
    private BigDecimal price;


}
