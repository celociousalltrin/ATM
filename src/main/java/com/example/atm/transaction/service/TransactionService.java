package com.example.atm.transaction.service;

import com.example.atm.account.dto.AccountOperationRequest;
import com.example.atm.account.entity.Account;
import com.example.atm.account.repository.AccountRepository;
import com.example.atm.account.service.AccountService;
import com.example.atm.common.exception.BusinessException;
import com.example.atm.common.exception.ResourceNotFoundException;
import com.example.atm.common.response.ResponseCode;
import com.example.atm.debt.dto.DebtRequest;
import com.example.atm.debt.dto.DebtResponse;
import com.example.atm.debt.service.DebtService;
import com.example.atm.transaction.dto.TransactionRequest;
import com.example.atm.transaction.dto.TransactionResponse;
import com.example.atm.transaction.entity.Transaction;
import com.example.atm.transaction.entity.TransactionType;
import com.example.atm.transaction.mapper.TransactionMapper;
import com.example.atm.transaction.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionService {
  private final TransactionRepository transactionRepository;
  private final AccountRepository accountRepository;
  private final AccountService accountService;
  private final DebtService debtService;
  private final TransactionMapper transactionMapper;

  public TransactionResponse createTransaction(TransactionRequest payload) {
    Transaction transaction = this.transactionMapper.toEntity(payload);
    Account account =
        this.accountRepository
            .findById(transaction.getAccountId())
            .orElseThrow(() -> new ResourceNotFoundException(ResponseCode.ACCOUNT_NOT_FOUND));

    this.validateTransaction(transaction, account);
    this.updateAccountBalance(transaction, account);
    Transaction createdTransaction = this.transactionRepository.save(transaction);
    return this.transactionMapper.toResponse(createdTransaction);
  }

  public void updateAccountBalance(Transaction transaction, Account account) {

    AccountOperationRequest accountOperationRequest =
        this.transactionMapper.toAccountOperationRequest(transaction);

    switch (transaction.getType()) {
      case DEPOSIT -> this.accountService.credit(accountOperationRequest);
      case WITHDRAW -> this.accountService.debit(accountOperationRequest);
      case TRANSFER -> {
        if (transaction.getAmount().compareTo(account.getBalance()) > 0) {
          this.createOrUpdateDebt(transaction, account);
          accountOperationRequest = this.transactionMapper.toAccountOperationRequest(transaction);
        }
        AccountOperationRequest targetAccountOperationRequest =
            this.transactionMapper.toTargetAccountOperationRequest(transaction);
        this.accountService.debit(accountOperationRequest);
        this.accountService.credit(targetAccountOperationRequest);
      }
      case DEBT_PAYMENT -> {
        AccountOperationRequest targetAccountOperationRequest =
            this.transactionMapper.toTargetAccountOperationRequest(transaction);
        DebtResponse debt =
            this.debtService.getDebtBetween(
                transaction.getAccountId(), transaction.getTargetAccountId());
        this.debtService.updateDebtStatus(debt.getId());

        this.accountService.debit(accountOperationRequest);
        this.accountService.credit(targetAccountOperationRequest);
      }
    }
  }

  private void createOrUpdateDebt(Transaction transaction, Account account) {
    BigDecimal debtAmount = transaction.getAmount().subtract(account.getBalance());
    DebtRequest debtRequest =
        this.transactionMapper.toSourceDebtRequest(
            transaction.getAccountId(), transaction.getTargetAccountId(), debtAmount);
    transaction.setAmount(account.getBalance());
    if (this.debtService.debtExistsBetween(
        transaction.getAccountId(), transaction.getTargetAccountId())) {
      DebtResponse debtResp =
          this.debtService.getDebtBetween(
              transaction.getAccountId(), transaction.getTargetAccountId());
      this.debtService.updateDebt(debtResp.getId(), debtAmount);
    } else {
      this.debtService.createDebt(debtRequest);
    }
  }

  private void validateTransaction(Transaction transaction, Account account) {

    String targetAccountId = transaction.getTargetAccountId();

    if (Set.of(TransactionType.TRANSFER, TransactionType.DEBT_PAYMENT)
        .contains(transaction.getType())) {
      if (targetAccountId == null || !this.accountRepository.isAccountExists(targetAccountId)) {
        throw new ResourceNotFoundException(ResponseCode.ACCOUNT_NOT_FOUND);
      }

      if (transaction.getAccountId().equals(targetAccountId)) {
        throw new BusinessException(ResponseCode.SAME_ACCOUNT_TRANSFER_NOT_ALLOWED);
      }
    }
    if (!TransactionType.DEPOSIT.equals(transaction.getType())) {
      if (account.getBalance().compareTo(BigDecimal.ZERO) == 0) {
        throw new BusinessException(ResponseCode.INSUFFICIENT_BALANCE);
      }
    }
    if (!Set.of(TransactionType.TRANSFER, TransactionType.DEPOSIT)
        .contains(transaction.getType())) {
      if (account.getBalance().compareTo(transaction.getAmount()) < 0) {
        throw new BusinessException(ResponseCode.INSUFFICIENT_BALANCE);
      }
    }
    if (TransactionType.DEBT_PAYMENT.equals(transaction.getType())) {
      DebtResponse debtResp =
          this.debtService.getDebtBetween(
              transaction.getAccountId(), transaction.getTargetAccountId());
      if (!debtResp.getAmount().equals(transaction.getAmount())) {
        throw new BusinessException(ResponseCode.TRANSACTION_AMOUNT_NOT_EQUAL_TO_DEBT_AMOUNT);
      }
    }
  }
}
