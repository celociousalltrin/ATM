package com.example.atm.debt.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.atm.debt.dto.DebtPayableResponse;
import com.example.atm.debt.dto.DebtRequest;
import com.example.atm.debt.dto.DebtResponse;
import com.example.atm.debt.service.DebtService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DebtController.class)
class DebtControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private DebtService debtService;

  @Test
  void debt_shouldReturnSuccess() throws Exception {

    DebtResponse response = org.mockito.Mockito.mock(DebtResponse.class);

    when(debtService.createDebt(any(DebtRequest.class))).thenReturn(response);

    String requestBody =
        """
                {
                  "owedBy": "account-123",
                  "owedTo": "account-456",
                  "amount": 100
                }
                """;

    mockMvc
        .perform(post("/debt").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isOk());
  }

  @Test
  void updateDebt_shouldReturnSuccess() throws Exception {

    DebtResponse response = org.mockito.Mockito.mock(DebtResponse.class);

    when(debtService.updateDebtStatus("debt-123")).thenReturn(response);

    mockMvc.perform(put("/debt/update/debt-123")).andExpect(status().isOk());
  }

  @Test
  void listPayableDebts_shouldReturnSuccess() throws Exception {

    List<DebtPayableResponse> response =
        List.of(org.mockito.Mockito.mock(DebtPayableResponse.class));

    when(debtService.getPayableDebts("account-123")).thenReturn(response);

    mockMvc.perform(get("/debt").param("accountId", "account-123")).andExpect(status().isOk());
  }
}
