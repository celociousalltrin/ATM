package com.example.atm.account;

import static org.junit.jupiter.api.Assertions.*;

import com.example.atm.account.entity.Account;
import com.example.atm.account.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountRepositoryTest {

  private AccountRepository accountRepository;

  private Account account;

  @BeforeEach
  void setUp() {
    accountRepository = new AccountRepository();

    account = new Account("user-123");
  }

  // ---------------------------------------------------------
  // SAVE
  // ---------------------------------------------------------

  @Test
  void save_shouldStoreAndReturnAccount() {

    Account result = accountRepository.save(account);

    assertNotNull(result);
    assertEquals(account, result);

    assertTrue(accountRepository.findById(account.getId()).isPresent());
  }

  // ---------------------------------------------------------
  // FIND BY ID
  // ---------------------------------------------------------

  @Test
  void findById_shouldReturnAccount_whenExists() {

    accountRepository.save(account);

    Optional<Account> result = accountRepository.findById(account.getId());

    assertTrue(result.isPresent());
    assertEquals(account, result.get());
  }

  @Test
  void findById_shouldReturnEmpty_whenNotExists() {

    Optional<Account> result = accountRepository.findById("invalid-account");

    assertTrue(result.isEmpty());
  }

  // ---------------------------------------------------------
  // ACCOUNT EXISTS
  // ---------------------------------------------------------

  @Test
  void isAccountExists_shouldReturnTrue_whenAccountExists() {

    accountRepository.save(account);

    assertTrue(accountRepository.isAccountExists(account.getId()));
  }

  @Test
  void isAccountExists_shouldReturnFalse_whenAccountDoesNotExist() {

    assertFalse(accountRepository.isAccountExists("invalid-account"));
  }

  // ---------------------------------------------------------
  // FIND BY USER ID
  // ---------------------------------------------------------

  @Test
  void findByUserId_shouldReturnAccount_whenUserHasAccount() {

    accountRepository.save(account);

    Optional<Account> result = accountRepository.findByUserId("user-123");

    assertTrue(result.isPresent());
    assertEquals(account, result.get());
  }

  @Test
  void findByUserId_shouldReturnEmpty_whenUserHasNoAccount() {

    Optional<Account> result = accountRepository.findByUserId("invalid-user");

    assertTrue(result.isEmpty());
  }

  // ---------------------------------------------------------
  // UPDATE
  // ---------------------------------------------------------

  @Test
  void update_shouldUpdateExistingAccount() {

    accountRepository.save(account);

    account.setBalance(new BigDecimal("1000"));

    Account result = accountRepository.update(account.getId(), account);

    assertEquals(account, result);
    assertEquals(new BigDecimal("1000"), result.getBalance());

    Account stored = accountRepository.findById(account.getId()).orElseThrow();

    assertEquals(new BigDecimal("1000"), stored.getBalance());
  }

  @Test
  void update_shouldStoreAccount_whenIdDoesNotExist() {

    Account newAccount = new Account("user-999");

    Account result = accountRepository.update(newAccount.getId(), newAccount);

    assertEquals(newAccount, result);

    assertTrue(accountRepository.findById(newAccount.getId()).isPresent());
  }
}
