package com.pockEtentertainmentApp.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

@Data
public class AddGameRequest {

    @NotBlank(message = "Name must not be blank!")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 symbols!")
    private String name;

    @NotBlank(message = "Description must not be blank!")
    @Size(min = 1, max = 100, message = "Description must be between 1 and 100 symbols!")
    private String description;

    @URL
    @NotBlank(message = "Image must not be blank!")
    private String imageUrl;

    @NotNull(message = "Price must not be empty!")
    private BigDecimal price;



}
