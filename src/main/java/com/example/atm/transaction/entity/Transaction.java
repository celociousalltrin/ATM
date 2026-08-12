package com.example.atm.transaction.entity;

import com.example.atm.common.util.AppUtil;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction {
  private final String id = AppUtil.generateUUID();
  private final String accountId;
  private BigDecimal amount;
  private final String targetAccountId;
  private final TransactionType type;
  private final Instant createdAt = Instant.now();

  public Transaction(
      String accountId, BigDecimal amount, String targetAccountId, TransactionType type) {
    this.accountId = accountId;
    this.amount = amount;
    this.targetAccountId = targetAccountId;
    this.type = type;
  }
}
