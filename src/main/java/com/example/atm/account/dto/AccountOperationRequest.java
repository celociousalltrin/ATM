package com.example.atm.account.dto;

import com.example.atm.common.response.ResponseCode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountOperationRequest {
  @NotBlank(message = ResponseCode.CONSTANTS.ER003)
  String id;

  @NotNull
  @DecimalMin(value = "0.01")
  @Digits(integer = 10, fraction = 2)
  BigDecimal balance;
}
