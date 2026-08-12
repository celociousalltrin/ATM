package com.example.atm.transaction;

import static org.junit.jupiter.api.Assertions.*;

import com.example.atm.transaction.entity.Transaction;
import com.example.atm.transaction.entity.TransactionType;
import com.example.atm.transaction.repository.TransactionRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionRepositoryTest {

  private TransactionRepository transactionRepository;

  @BeforeEach
  void setUp() {
    transactionRepository = new TransactionRepository();
  }

  @Test
  void save_shouldSaveTransaction() {
    Transaction transaction =
        new Transaction("account-123", new BigDecimal("100"), null, TransactionType.DEPOSIT);

    Transaction result = transactionRepository.save(transaction);

    assertNotNull(result);
    assertEquals(transaction.getId(), result.getId());
  }

  @Test
  void findById_shouldReturnTransaction_whenExists() {
    Transaction transaction =
        new Transaction("account-123", new BigDecimal("100"), null, TransactionType.DEPOSIT);

    transactionRepository.save(transaction);

    var result = transactionRepository.findById(transaction.getId());

    assertTrue(result.isPresent());
    assertEquals(transaction.getId(), result.get().getId());
  }

  @Test
  void findById_shouldReturnEmpty_whenNotExists() {
    var result = transactionRepository.findById("invalid-id");

    assertTrue(result.isEmpty());
  }

  @Test
  void findByAccountId_shouldReturnTransaction_whenExists() {
    Transaction transaction =
        new Transaction("account-123", new BigDecimal("100"), null, TransactionType.DEPOSIT);

    transactionRepository.save(transaction);

    var result = transactionRepository.findByAccountId("account-123");

    assertTrue(result.isPresent());
    assertEquals("account-123", result.get().getAccountId());
  }

  @Test
  void findByAccountId_shouldReturnEmpty_whenNotExists() {
    var result = transactionRepository.findByAccountId("invalid-account");

    assertTrue(result.isEmpty());
  }
}
