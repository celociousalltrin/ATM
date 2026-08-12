package com.example.atm.transaction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.atm.account.dto.AccountOperationRequest;
import com.example.atm.account.entity.Account;
import com.example.atm.account.repository.AccountRepository;
import com.example.atm.account.service.AccountService;
import com.example.atm.common.exception.BusinessException;
import com.example.atm.common.exception.ResourceNotFoundException;
import com.example.atm.debt.service.DebtService;
import com.example.atm.transaction.dto.TransactionRequest;
import com.example.atm.transaction.dto.TransactionResponse;
import com.example.atm.transaction.entity.Transaction;
import com.example.atm.transaction.entity.TransactionType;
import com.example.atm.transaction.mapper.TransactionMapper;
import com.example.atm.transaction.repository.TransactionRepository;
import com.example.atm.transaction.service.TransactionService;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

  @Mock private TransactionRepository transactionRepository;

  @Mock private AccountRepository accountRepository;

  @Mock private AccountService accountService;

  @Mock private DebtService debtService;

  @Mock private TransactionMapper transactionMapper;

  @InjectMocks private TransactionService transactionService;

  private Account account;

  @BeforeEach
  void setUp() {
    account = new Account("user-123");
  }

  @Test
  void createTransaction_shouldCreateDepositSuccessfully() {

    TransactionRequest request = mock(TransactionRequest.class);

    Transaction transaction =
        new Transaction(account.getId(), new BigDecimal("100"), null, TransactionType.DEPOSIT);
    AccountOperationRequest operationRequest = new AccountOperationRequest();

    when(transactionMapper.toAccountOperationRequest(transaction)).thenReturn(operationRequest);

    TransactionResponse response = mock(TransactionResponse.class);

    when(transactionMapper.toEntity(request)).thenReturn(transaction);
    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    when(transactionRepository.save(transaction)).thenReturn(transaction);

    when(transactionMapper.toResponse(transaction)).thenReturn(response);

    TransactionResponse result = transactionService.createTransaction(request);

    assertNotNull(result);
    assertEquals(response, result);

    verify(transactionMapper).toEntity(request);
    verify(accountRepository).findById(account.getId());
    verify(accountService).credit(any(AccountOperationRequest.class));
    verify(transactionRepository).save(transaction);
    verify(transactionMapper).toResponse(transaction);
  }

  @Test
  void createTransaction_shouldThrowException_whenAccountDoesNotExist() {

    TransactionRequest request = mock(TransactionRequest.class);

    Transaction transaction =
        new Transaction("invalid-account", new BigDecimal("100"), null, TransactionType.DEPOSIT);

    when(transactionMapper.toEntity(request)).thenReturn(transaction);

    when(accountRepository.findById("invalid-account")).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class, () -> transactionService.createTransaction(request));

    verify(transactionRepository, never()).save(any());
  }

  @Test
  void createTransaction_shouldThrowException_whenWithdrawWithZeroBalance() {

    TransactionRequest request = mock(TransactionRequest.class);

    Transaction transaction =
        new Transaction(account.getId(), new BigDecimal("100"), null, TransactionType.WITHDRAW);

    when(transactionMapper.toEntity(request)).thenReturn(transaction);

    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    assertThrows(BusinessException.class, () -> transactionService.createTransaction(request));

    verify(transactionRepository, never()).save(any());
  }

  @Test
  void createTransaction_shouldThrowException_whenWithdrawAmountGreaterThanBalance() {

    account.setBalance(new BigDecimal("50"));

    TransactionRequest request = mock(TransactionRequest.class);

    Transaction transaction =
        new Transaction(account.getId(), new BigDecimal("100"), null, TransactionType.WITHDRAW);

    when(transactionMapper.toEntity(request)).thenReturn(transaction);

    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    assertThrows(BusinessException.class, () -> transactionService.createTransaction(request));

    verify(transactionRepository, never()).save(any());
  }

  @Test
  void createTransaction_shouldThrowException_whenTransferTargetAccountDoesNotExist() {

    account.setBalance(new BigDecimal("500"));

    TransactionRequest request = mock(TransactionRequest.class);

    Transaction transaction =
        new Transaction(
            account.getId(), new BigDecimal("100"), "target-account", TransactionType.TRANSFER);

    when(transactionMapper.toEntity(request)).thenReturn(transaction);

    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    when(accountRepository.isAccountExists("target-account")).thenReturn(false);

    assertThrows(
        ResourceNotFoundException.class, () -> transactionService.createTransaction(request));

    verify(transactionRepository, never()).save(any());
  }

  @Test
  void createTransaction_shouldThrowException_whenTransferToSameAccount() {

    account.setBalance(new BigDecimal("500"));

    TransactionRequest request = mock(TransactionRequest.class);

    Transaction transaction =
        new Transaction(
            account.getId(), new BigDecimal("100"), account.getId(), TransactionType.TRANSFER);

    when(transactionMapper.toEntity(request)).thenReturn(transaction);

    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    when(accountRepository.isAccountExists(account.getId())).thenReturn(true);

    assertThrows(BusinessException.class, () -> transactionService.createTransaction(request));

    verify(transactionRepository, never()).save(any());
  }

  @Test
  void createTransaction_shouldWithdrawAndCredit_whenTransferIsSuccessful() {

    account.setBalance(new BigDecimal("500"));

    TransactionRequest request = mock(TransactionRequest.class);

    Transaction transaction =
        new Transaction(
            account.getId(), new BigDecimal("100"), "target-account", TransactionType.TRANSFER);

    TransactionResponse response = mock(TransactionResponse.class);

    AccountOperationRequest sourceOperation = mock(AccountOperationRequest.class);

    AccountOperationRequest targetOperation = mock(AccountOperationRequest.class);

    when(transactionMapper.toEntity(request)).thenReturn(transaction);

    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    when(accountRepository.isAccountExists("target-account")).thenReturn(true);

    when(transactionMapper.toAccountOperationRequest(transaction)).thenReturn(sourceOperation);

    when(transactionMapper.toTargetAccountOperationRequest(transaction))
        .thenReturn(targetOperation);

    when(transactionRepository.save(transaction)).thenReturn(transaction);

    when(transactionMapper.toResponse(transaction)).thenReturn(response);

    TransactionResponse result = transactionService.createTransaction(request);

    assertEquals(response, result);

    verify(accountService).debit(sourceOperation);
    verify(accountService).credit(targetOperation);
    verify(transactionRepository).save(transaction);
  }

  @Test
  void createTransaction_shouldCreateDebt_whenTransferAmountExceedsBalance() {

    account.setBalance(new BigDecimal("50"));

    TransactionRequest request = mock(TransactionRequest.class);

    Transaction transaction =
        new Transaction(
            account.getId(), new BigDecimal("100"), "target-account", TransactionType.TRANSFER);

    TransactionResponse response = mock(TransactionResponse.class);

    AccountOperationRequest sourceOperation = mock(AccountOperationRequest.class);

    AccountOperationRequest targetOperation = mock(AccountOperationRequest.class);

    when(transactionMapper.toEntity(request)).thenReturn(transaction);

    when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

    when(accountRepository.isAccountExists("target-account")).thenReturn(true);

    when(transactionMapper.toAccountOperationRequest(transaction)).thenReturn(sourceOperation);

    when(transactionMapper.toTargetAccountOperationRequest(transaction))
        .thenReturn(targetOperation);

    when(debtService.debtExistsBetween(account.getId(), "target-account")).thenReturn(false);

    when(transactionMapper.toSourceDebtRequest(
            eq(account.getId()), eq("target-account"), eq(new BigDecimal("50"))))
        .thenReturn(null);

    when(transactionRepository.save(transaction)).thenReturn(transaction);

    when(transactionMapper.toResponse(transaction)).thenReturn(response);

    TransactionResponse result = transactionService.createTransaction(request);

    assertEquals(response, result);

    verify(debtService).createDebt(any());
    verify(accountService).debit(sourceOperation);
    verify(accountService).credit(targetOperation);

    // Original transaction amount becomes available balance
    assertEquals(new BigDecimal("50"), transaction.getAmount());
  }
}
