package com.example.atm.account.entity;

import com.example.atm.common.util.AppUtil;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {
  private final String id = AppUtil.generateUUID();
  private final String userId;
  private BigDecimal balance = BigDecimal.ZERO;
  private final Instant createdAt = Instant.now();
  private Instant updatedAt = Instant.now();

  public Account(String userId) {
    this.userId = userId;
  }
}
