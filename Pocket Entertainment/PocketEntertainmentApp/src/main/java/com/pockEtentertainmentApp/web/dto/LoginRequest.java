package com.pockEtentertainmentApp.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Please enter your username")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters!")
    private String username;

    @NotBlank(message = "Please enter your password.")
    @Size(min = 3, max = 20, message = "Password must be between 3 and 20 characters!")
    private String password;


}
