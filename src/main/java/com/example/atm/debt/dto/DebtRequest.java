package com.example.atm.debt.dto;

import com.example.atm.common.response.ResponseCode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DebtRequest {
  @NotEmpty(message = ResponseCode.CONSTANTS.ER006)
  private String owedBy;

  @NotEmpty(message = ResponseCode.CONSTANTS.ER006)
  private String owedTo;

  @NotNull
  @DecimalMin(value = "0.01")
  @Digits(integer = 10, fraction = 2)
  private BigDecimal amount;
}
