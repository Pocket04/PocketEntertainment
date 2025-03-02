package com.pockEtentertainmentApp.web.dto;

import com.pockEtentertainmentApp.game.model.Game;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddReviewRequest {

    @Size(max = 1000, message = "The review must be shorter than 1000 symbols!")
    private String body;

    @Min(value = 1, message = "Minimum 1 star required.")
    @Max(10)
    private int rating;

    @NotNull(message = "Please select a game.")
    private Game game;


}
