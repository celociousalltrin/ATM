package com.example.atm.debt.controller;

import com.example.atm.common.response.ApiResponse;
import com.example.atm.common.response.ResponseCode;
import com.example.atm.debt.dto.DebtPayableResponse;
import com.example.atm.debt.dto.DebtRequest;
import com.example.atm.debt.dto.DebtResponse;
import com.example.atm.debt.service.DebtService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/debt")
@AllArgsConstructor
public class DebtController {
  private final DebtService debtService;

  @PostMapping
  public ApiResponse<DebtResponse> debt(@Valid @RequestBody DebtRequest payload) {
    return ApiResponse.success(ResponseCode.DEBT_CREATED, this.debtService.createDebt(payload));
  }

  @PutMapping("/update/{id}")
  public ApiResponse<DebtResponse> update(@PathVariable("id") String id) {
    return ApiResponse.success(ResponseCode.DEBT_UPDATED, this.debtService.updateDebtStatus(id));
  }

  @GetMapping
  public ApiResponse<List<DebtPayableResponse>> listPayableDebts(
      @RequestParam("accountId") String accountId) {
    return ApiResponse.success(ResponseCode.SUCCESS, this.debtService.getPayableDebts(accountId));
  }
}
