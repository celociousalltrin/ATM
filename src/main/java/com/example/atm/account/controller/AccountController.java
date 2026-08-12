package com.example.atm.account.controller;

import com.example.atm.account.dto.AccountOperationRequest;
import com.example.atm.account.dto.AccountRequest;
import com.example.atm.account.dto.AccountResponse;
import com.example.atm.account.service.AccountService;
import com.example.atm.common.response.ApiResponse;
import com.example.atm.common.response.ResponseCode;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {
  private final AccountService accountService;

  @PostMapping
  public ApiResponse<AccountResponse> account(@Valid @RequestBody AccountRequest payload) {
    AccountResponse account = this.accountService.getAccountByUserId(payload.getUserId());
    if (account == null) {
      account = this.accountService.createAccount(payload);
    }
    return ApiResponse.success(ResponseCode.ACCOUNT_CREATED, account);
  }

  @GetMapping("/{id}")
  public ApiResponse<AccountResponse> getAccountById(@PathVariable("id") String id) {
    return ApiResponse.success(ResponseCode.SUCCESS, this.accountService.getAccountById(id));
  }

  @GetMapping("/user/{userId}")
  public ApiResponse<AccountResponse> getAccountByUserId(@PathVariable("userId") String userId) {
    return ApiResponse.success(
        ResponseCode.SUCCESS, this.accountService.getAccountByUserId(userId));
  }

  @PutMapping("/deposit")
  public ApiResponse<AccountResponse> deposit(@Valid @RequestBody AccountOperationRequest payload) {
    return ApiResponse.success(ResponseCode.AMOUNT_DEPOSITED, this.accountService.credit(payload));
  }

  @PutMapping("/withdraw")
  public ApiResponse<AccountResponse> withdraw(@RequestBody AccountOperationRequest payload) {
    return ApiResponse.success(ResponseCode.AMOUNT_WITHDRAW, this.accountService.debit(payload));
  }
}
