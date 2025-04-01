package app.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddCurrencyRequest {

    @NotNull(message = "Please enter amount!")
    @Min(value = 1, message = "Minimum amount is 1.")
    private BigDecimal amount;

}
