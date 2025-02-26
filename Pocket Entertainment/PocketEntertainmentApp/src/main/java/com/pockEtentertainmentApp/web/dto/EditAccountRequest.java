package com.pockEtentertainmentApp.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditAccountRequest {

    @URL
    private String profilePicture;

    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 symbols!")
    private String firstName;

    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 symbols!")
    private String lastName;

    @Email
    private String email;

}
