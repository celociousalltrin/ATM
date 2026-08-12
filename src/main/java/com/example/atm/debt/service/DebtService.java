package com.example.atm.debt.service;

import com.example.atm.account.entity.Account;
import com.example.atm.account.repository.AccountRepository;
import com.example.atm.common.exception.BusinessException;
import com.example.atm.common.exception.ResourceNotFoundException;
import com.example.atm.common.response.ResponseCode;
import com.example.atm.debt.dto.DebtPayableResponse;
import com.example.atm.debt.dto.DebtRequest;
import com.example.atm.debt.dto.DebtResponse;
import com.example.atm.debt.entity.Debt;
import com.example.atm.debt.entity.DebtStatus;
import com.example.atm.debt.mapper.DebtMapper;
import com.example.atm.debt.repository.DebtRepository;
import com.example.atm.user.entity.User;
import com.example.atm.user.repository.UserRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DebtService {
  private final DebtRepository debtRepository;
  private final AccountRepository accountRepository;
  private final UserRepository userRepository;
  private final DebtMapper debtMapper;

  public DebtResponse createDebt(DebtRequest debtRequest) {
    if (!this.accountRepository.isAccountExists(debtRequest.getOwedBy())
        || !this.accountRepository.isAccountExists(debtRequest.getOwedTo())) {
      throw new ResourceNotFoundException(ResponseCode.ACCOUNT_NOT_FOUND);
    }
    Debt debt = this.debtMapper.toEntity(debtRequest);
    Debt createdDebt = this.debtRepository.save(debt);
    return this.debtMapper.toResponse(createdDebt);
  }

  public DebtResponse updateDebtStatus(String id) {
    Debt debt =
        this.debtRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(ResponseCode.DEBT_NOT_FOUND));

    if (DebtStatus.COMPLETED.equals(debt.getStatus())) {
      throw new BusinessException(ResponseCode.DEBT_ALREADY_PAID);
    }
    debt.setStatus(DebtStatus.COMPLETED);
    debt.setUpdatedAt(Instant.now());
    this.debtRepository.update(id, debt);
    return this.debtMapper.toResponse(debt);
  }

  public List<DebtPayableResponse> getPayableDebts(String accountId) {
    Function<Debt, DebtPayableResponse> debtPayableResponseFunction =
        (debt) -> {
          Account account =
              this.accountRepository
                  .findById(debt.getOwedBy())
                  .orElseThrow(() -> new ResourceNotFoundException(ResponseCode.ACCOUNT_NOT_FOUND));
          User user =
              this.userRepository
                  .findById(account.getUserId())
                  .orElseThrow(() -> new ResourceNotFoundException(ResponseCode.USER_ID_NOT_FOUND));
          return this.debtMapper.toDebtPayableResponse(debt, user);
        };

    return this.debtRepository.findByOwedBy(accountId).stream()
        .map(debtPayableResponseFunction)
        .toList();
  }

  public DebtResponse updateDebt(String id, BigDecimal amount) {
    Debt debt =
        this.debtRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(ResponseCode.DEBT_NOT_FOUND));
    debt.setAmount(amount);
    debt.setStatus(DebtStatus.PENDING);
    this.debtRepository.update(id, debt);
    return this.debtMapper.toResponse(debt);
  }

  public DebtResponse getDebtBetween(String owedBy, String owedTo) {
    Debt debt =
        this.debtRepository
            .findByOwedByAndOwedToID(owedBy, owedTo)
            .orElseThrow(() -> new ResourceNotFoundException(ResponseCode.DEBT_NOT_FOUND));
    return this.debtMapper.toResponse(debt);
  }

  public boolean debtExistsBetween(String owedBy, String owedTo) {
    Debt debt = this.debtRepository.findByOwedByAndOwedToID(owedBy, owedTo).orElse(null);
    return debt != null;
  }
}
