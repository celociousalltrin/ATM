package com.example.atm.account.dto;

import com.example.atm.common.response.ResponseCode;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {
  @NotEmpty(message = ResponseCode.CONSTANTS.ER006)
  private String userId;
}
