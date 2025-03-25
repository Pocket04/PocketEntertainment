package app.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Please enter your email.")
    @Email
    private String email;

    @NotBlank(message = "Please enter your username.")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 symbols!")
    private String username;

    @NotBlank(message = "Please enter your password.")
    @Size(min = 3, max = 20, message = "Password must be between 3 and 20 symbols!")
    private String password;







}
