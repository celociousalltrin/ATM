package com.example.atm.debt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.atm.account.repository.AccountRepository;
import com.example.atm.debt.dto.DebtRequest;
import com.example.atm.debt.dto.DebtResponse;
import com.example.atm.debt.entity.Debt;
import com.example.atm.debt.mapper.DebtMapper;
import com.example.atm.debt.repository.DebtRepository;
import com.example.atm.debt.service.DebtService;
import com.example.atm.user.repository.UserRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DebtServiceTest {

  @Mock private DebtRepository debtRepository;

  @Mock private AccountRepository accountRepository;

  @Mock private UserRepository userRepository;

  @Mock private DebtMapper debtMapper;

  private DebtService debtService;

  @BeforeEach
  void setUp() {

    MockitoAnnotations.openMocks(this);

    debtService = new DebtService(debtRepository, accountRepository, userRepository, debtMapper);
  }

  @Test
  void createDebt_shouldCreateDebt() {

    DebtRequest request = new DebtRequest();

    request.setOwedBy("account-123");
    request.setOwedTo("account-456");

    Debt debt = new Debt("account-123", "account-456", new BigDecimal("100"));

    DebtResponse response = org.mockito.Mockito.mock(DebtResponse.class);

    when(accountRepository.isAccountExists("account-123")).thenReturn(true);

    when(accountRepository.isAccountExists("account-456")).thenReturn(true);

    when(debtMapper.toEntity(any(DebtRequest.class))).thenReturn(debt);

    when(debtRepository.save(debt)).thenReturn(debt);

    when(debtMapper.toResponse(debt)).thenReturn(response);

    DebtResponse result = debtService.createDebt(request);

    assertEquals(response, result);
  }
}
