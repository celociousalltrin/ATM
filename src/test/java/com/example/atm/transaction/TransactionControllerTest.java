package com.example.atm.transaction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.atm.transaction.controller.TransactionController;
import com.example.atm.transaction.dto.TransactionRequest;
import com.example.atm.transaction.dto.TransactionResponse;
import com.example.atm.transaction.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TransactionService transactionService;

  @Test
  void transaction_shouldReturnSuccess() throws Exception {

    TransactionResponse response =
            org.mockito.Mockito.mock(TransactionResponse.class);

    when(transactionService.createTransaction(any(TransactionRequest.class)))
            .thenReturn(response);

    String requestBody = """
                {
                    "accountId": "account-123",
                    "amount": 100,
                    "type": "DEPOSIT"
                }
                """;

    mockMvc.perform(
                    post("/transaction")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
            )
            .andExpect(status().isOk());
  }
}