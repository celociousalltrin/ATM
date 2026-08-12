package com.example.atm.debt.dto;

import com.example.atm.debt.entity.DebtStatus;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DebtResponse {
  private String id;
  private String owedBy;
  private String owedTo;
  private BigDecimal amount;
  private DebtStatus status;
  private Instant createdAt;
  private Instant updatedAt;
}
