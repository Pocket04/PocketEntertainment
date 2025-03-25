package app.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddCurrencyRequest {

    @NotNull(message = "Please enter amount!")
    private BigDecimal amount;

}
