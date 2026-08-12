package com.example.atm.debt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.atm.debt.entity.Debt;
import com.example.atm.debt.repository.DebtRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DebtRepositoryTest {

  private DebtRepository debtRepository;

  @BeforeEach
  void setUp() {
    debtRepository = new DebtRepository();
  }

  @Test
  void save_shouldSaveDebt() {

    Debt debt = new Debt("account-123", "account-456", new BigDecimal("100"));

    Debt result = debtRepository.save(debt);

    assertEquals(debt, result);
    assertTrue(debtRepository.findById(debt.getId()).isPresent());
  }

  @Test
  void findById_shouldReturnDebt() {

    Debt debt = new Debt("account-123", "account-456", new BigDecimal("100"));

    debtRepository.save(debt);

    var result = debtRepository.findById(debt.getId());

    assertTrue(result.isPresent());
    assertEquals(debt.getId(), result.get().getId());
  }

  @Test
  void findByOwedBy_shouldReturnDebts() {

    Debt debt = new Debt("account-123", "account-456", new BigDecimal("100"));

    debtRepository.save(debt);

    List<Debt> result = debtRepository.findByOwedBy("account-123");

    assertEquals(1, result.size());
    assertEquals(debt.getId(), result.get(0).getId());
  }
}
