package com.example.atm.debt.entity;

import com.example.atm.common.util.AppUtil;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Debt {
  private final String id = AppUtil.generateUUID();
  private final String owedBy;
  private final String owedTo;
  private BigDecimal amount;
  private DebtStatus status = DebtStatus.PENDING;
  private final Instant createdAt = Instant.now();
  private Instant updatedAt = Instant.now();

  public Debt(String owedBy, String owedTo, BigDecimal amount) {
    this.owedBy = owedBy;
    this.owedTo = owedTo;
    this.amount = amount;
  }
}
