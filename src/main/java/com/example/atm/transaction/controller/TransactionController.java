package com.example.atm.transaction.controller;

import com.example.atm.common.response.ApiResponse;
import com.example.atm.common.response.ResponseCode;
import com.example.atm.transaction.dto.TransactionRequest;
import com.example.atm.transaction.dto.TransactionResponse;
import com.example.atm.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@AllArgsConstructor
public class TransactionController {
  private final TransactionService transactionService;

  @PostMapping
  public ApiResponse<TransactionResponse> transaction(
      @Valid @RequestBody TransactionRequest transactionRequest) {
    return ApiResponse.success(
        ResponseCode.TRANSACTION_CREATED,
        this.transactionService.createTransaction(transactionRequest));
  }
}
