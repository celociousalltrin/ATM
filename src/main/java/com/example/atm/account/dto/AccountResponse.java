package com.example.atm.account.dto;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
  private String id;
  private String userId;
  private BigDecimal balance;
  private Instant createdAt;
  private Instant updatedAt;
}
