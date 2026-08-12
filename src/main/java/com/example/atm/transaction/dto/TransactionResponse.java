package com.example.atm.transaction.dto;

import com.example.atm.transaction.entity.TransactionType;
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
public class TransactionResponse {
  private String id;
  private String accountId;
  private BigDecimal amount;
  private String targetAccounId;
  private TransactionType type;
  private Instant createdAt;
}
