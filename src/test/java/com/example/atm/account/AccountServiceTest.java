package com.example.atm.account;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.atm.account.dto.AccountOperationRequest;
import com.example.atm.account.dto.AccountRequest;
import com.example.atm.account.dto.AccountResponse;
import com.example.atm.account.entity.Account;
import com.example.atm.account.mapper.AccountMapper;
import com.example.atm.account.repository.AccountRepository;
import com.example.atm.account.service.AccountService;
import com.example.atm.common.exception.ResourceNotFoundException;
import com.example.atm.user.repository.UserRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  @Mock private AccountRepository accountRepository;

  @Mock private UserRepository userRepository;

  @Mock private AccountMapper accountMapper;

  @InjectMocks private AccountService accountService;

  private Account account;
  private AccountResponse accountResponse;

  @BeforeEach
  void setUp() {
    account = new Account("user-123");
    accountResponse = mock(AccountResponse.class);
  }

  // ---------------------------------------------------------
  // CREATE ACCOUNT
  // ---------------------------------------------------------

  @Test
  void createAccount_shouldCreateAccount_whenUserExists() {

    AccountRequest request = mock(AccountRequest.class);

    when(request.getUserId()).thenReturn("user-123");
    when(userRepository.isPresentById("user-123")).thenReturn(true);

    when(accountMapper.toEntity(request)).thenReturn(account);
    when(accountRepository.save(account)).thenReturn(account);
    when(accountMapper.toResponse(account)).thenReturn(accountResponse);

    AccountResponse result = accountService.createAccount(request);

    assertNotNull(result);
    assertEquals(accountResponse, result);

    verify(userRepository).isPresentById("user-123");
    verify(accountMapper).toEntity(request);
    verify(accountRepository).save(account);
    verify(accountMapper).toResponse(account);
  }

  @Test
  void createAccount_shouldThrowException_whenUserDoesNotExist() {

    AccountRequest request = mock(AccountRequest.class);

    when(request.getUserId()).thenReturn("invalid-user");
    when(userRepository.isPresentById("invalid-user")).thenReturn(false);

    assertThrows(ResourceNotFoundException.class, () -> accountService.createAccount(request));

    verify(userRepository).isPresentById("invalid-user");

    verify(accountMapper, never()).toEntity(any());
    verify(accountRepository, never()).save(any());
  }

  // ---------------------------------------------------------
  // GET ACCOUNT BY ID
  // ---------------------------------------------------------

  @Test
  void getAccountById_shouldReturnAccount_whenAccountExists() {

    String accountId = account.getId();

    when(accountRepository.findById(accountId)).thenReturn(java.util.Optional.of(account));

    when(accountMapper.toResponse(account)).thenReturn(accountResponse);

    AccountResponse result = accountService.getAccountById(accountId);

    assertNotNull(result);
    assertEquals(accountResponse, result);

    verify(accountRepository).findById(accountId);
    verify(accountMapper).toResponse(account);
  }

  @Test
  void getAccountById_shouldThrowException_whenAccountDoesNotExist() {

    String accountId = "invalid-account";

    when(accountRepository.findById(accountId)).thenReturn(java.util.Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountById(accountId));

    verify(accountRepository).findById(accountId);
    verify(accountMapper, never()).toResponse(any());
  }

  // ---------------------------------------------------------
  // GET ACCOUNT BY USER ID
  // ---------------------------------------------------------

  @Test
  void getAccountByUserId_shouldReturnAccount_whenUserAndAccountExist() {

    String userId = "user-123";

    when(userRepository.isPresentById(userId)).thenReturn(true);

    when(accountRepository.findByUserId(userId)).thenReturn(java.util.Optional.of(account));

    when(accountMapper.toResponse(account)).thenReturn(accountResponse);

    AccountResponse result = accountService.getAccountByUserId(userId);

    assertNotNull(result);
    assertEquals(accountResponse, result);

    verify(userRepository).isPresentById(userId);
    verify(accountRepository).findByUserId(userId);
    verify(accountMapper).toResponse(account);
  }

  @Test
  void getAccountByUserId_shouldThrowException_whenUserDoesNotExist() {

    String userId = "invalid-user";

    when(userRepository.isPresentById(userId)).thenReturn(false);

    assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountByUserId(userId));

    verify(userRepository).isPresentById(userId);
    verify(accountRepository, never()).findByUserId(any());
  }

  @Test
  void getAccountByUserId_shouldReturnNull_whenAccountDoesNotExist() {

    String userId = "user-123";

    when(userRepository.isPresentById(userId)).thenReturn(true);

    when(accountRepository.findByUserId(userId)).thenReturn(java.util.Optional.empty());

    AccountResponse result = accountService.getAccountByUserId(userId);

    assertNull(result);

    verify(userRepository).isPresentById(userId);
    verify(accountRepository).findByUserId(userId);
  }

  // ---------------------------------------------------------
  // CREDIT
  // ---------------------------------------------------------

  @Test
  void credit_shouldIncreaseBalance_whenAccountExists() {

    AccountOperationRequest request = mock(AccountOperationRequest.class);

    when(request.getId()).thenReturn(account.getId());
    when(request.getBalance()).thenReturn(new BigDecimal("500"));

    account.setBalance(new BigDecimal("1000"));

    when(accountRepository.findById(account.getId())).thenReturn(java.util.Optional.of(account));

    when(accountRepository.update(account.getId(), account)).thenReturn(account);

    when(accountMapper.toResponse(account)).thenReturn(accountResponse);

    AccountResponse result = accountService.credit(request);

    assertNotNull(result);
    assertEquals(new BigDecimal("1500"), account.getBalance());

    verify(accountRepository).findById(account.getId());
    verify(accountRepository).update(account.getId(), account);
    verify(accountMapper).toResponse(account);
  }

  @Test
  void credit_shouldThrowException_whenAccountDoesNotExist() {

    AccountOperationRequest request = mock(AccountOperationRequest.class);

    when(request.getId()).thenReturn("invalid-account");

    when(accountRepository.findById("invalid-account")).thenReturn(java.util.Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> accountService.credit(request));

    verify(accountRepository).findById("invalid-account");
    verify(accountRepository, never()).update(any(), any());
  }

  // ---------------------------------------------------------
  // DEBIT
  // ---------------------------------------------------------

  @Test
  void debit_shouldDecreaseBalance_whenAccountExists() {

    AccountOperationRequest request = mock(AccountOperationRequest.class);

    when(request.getId()).thenReturn(account.getId());
    when(request.getBalance()).thenReturn(new BigDecimal("300"));

    account.setBalance(new BigDecimal("1000"));

    when(accountRepository.findById(account.getId())).thenReturn(java.util.Optional.of(account));

    when(accountRepository.update(account.getId(), account)).thenReturn(account);

    when(accountMapper.toResponse(account)).thenReturn(accountResponse);

    AccountResponse result = accountService.debit(request);

    assertNotNull(result);

    assertEquals(new BigDecimal("700"), account.getBalance());

    verify(accountRepository).findById(account.getId());
    verify(accountRepository).update(account.getId(), account);
    verify(accountMapper).toResponse(account);
  }

  @Test
  void debit_shouldThrowException_whenAccountDoesNotExist() {

    AccountOperationRequest request = mock(AccountOperationRequest.class);

    when(request.getId()).thenReturn("invalid-account");

    when(accountRepository.findById("invalid-account")).thenReturn(java.util.Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> accountService.debit(request));

    verify(accountRepository).findById("invalid-account");
    verify(accountRepository, never()).update(any(), any());
  }
}
