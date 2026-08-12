package com.example.atm.transaction.dto;

import com.example.atm.common.response.ResponseCode;
import com.example.atm.transaction.entity.TransactionType;
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
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
  @NotEmpty(message = ResponseCode.CONSTANTS.ER006)
  private String accountId;

  @NotNull
  @DecimalMin(value = "0.01")
  @Digits(integer = 10, fraction = 2)
  private BigDecimal amount;

  private String targetAccountId;

  @NotNull(message = ResponseCode.CONSTANTS.ER005)
  private TransactionType type;
}
