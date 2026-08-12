package com.example.atm.account.service;

import com.example.atm.account.dto.AccountOperationRequest;
import com.example.atm.account.dto.AccountRequest;
import com.example.atm.account.dto.AccountResponse;
import com.example.atm.account.entity.Account;
import com.example.atm.account.mapper.AccountMapper;
import com.example.atm.account.repository.AccountRepository;
import com.example.atm.common.exception.ResourceNotFoundException;
import com.example.atm.common.response.ResponseCode;
import com.example.atm.user.repository.UserRepository;
import java.time.Instant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {
  private final AccountRepository accountRepository;
  private final UserRepository userRepository;
  private final AccountMapper accountMapper;

  public AccountResponse createAccount(AccountRequest accountRequest) {
    if (!this.userRepository.isPresentById(accountRequest.getUserId())) {
      throw new ResourceNotFoundException(ResponseCode.USER_ID_NOT_FOUND);
    }

    Account account = this.accountMapper.toEntity(accountRequest);
    Account createdAccount = this.accountRepository.save(account);
    return this.accountMapper.toResponse(createdAccount);
  }

  public AccountResponse getAccountById(String id) {
    return this.accountRepository
        .findById(id)
        .map(this.accountMapper::toResponse)
        .orElseThrow(() -> new ResourceNotFoundException(ResponseCode.ACCOUNT_NOT_FOUND));
  }

  public AccountResponse getAccountByUserId(String id) {
    if (!this.userRepository.isPresentById(id)) {
      throw new ResourceNotFoundException(ResponseCode.USER_ID_NOT_FOUND);
    }

    return this.accountRepository.findByUserId(id).map(this.accountMapper::toResponse).orElse(null);
  }

  public AccountResponse credit(AccountOperationRequest accountOperationRequest) {
    Account account =
        this.accountRepository
            .findById(accountOperationRequest.getId())
            .orElseThrow(() -> new ResourceNotFoundException(ResponseCode.ACCOUNT_NOT_FOUND));
    account.setBalance(account.getBalance().add(accountOperationRequest.getBalance()));
    account.setUpdatedAt(Instant.now());
    Account updatedAccount =
        this.accountRepository.update(accountOperationRequest.getId(), account);
    return this.accountMapper.toResponse(updatedAccount);
  }

  public AccountResponse debit(AccountOperationRequest accountOperationRequest) {
    Account account =
        this.accountRepository
            .findById(accountOperationRequest.getId())
            .orElseThrow(() -> new ResourceNotFoundException(ResponseCode.ACCOUNT_NOT_FOUND));

    account.setBalance(account.getBalance().subtract(accountOperationRequest.getBalance()));
    account.setUpdatedAt(Instant.now());
    Account updatedAccount =
        this.accountRepository.update(accountOperationRequest.getId(), account);
    return this.accountMapper.toResponse(updatedAccount);
  }
}
