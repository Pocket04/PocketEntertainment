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

    @NotBlank(message = "Name must not be blank!")
    private String name;
    @NotBlank(message = "Image must not be blank!")
    @URL
    private String imageUrl;
    @NotBlank(message = "Description must not be blank!")
    private String description;
    @NotNull(message = "Please select a game!")
    private UUID game;
    @NotNull(message = "Price must not be empty!")
    private BigDecimal price;


}
