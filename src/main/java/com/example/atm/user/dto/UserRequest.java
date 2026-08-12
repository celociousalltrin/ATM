package com.example.atm.user.dto;

import com.example.atm.common.response.ResponseCode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
  @NotBlank(message = ResponseCode.CONSTANTS.ER001)
  private String userName;
}
