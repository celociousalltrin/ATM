package com.example.atm.account;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.atm.account.controller.AccountController;
import com.example.atm.account.dto.AccountOperationRequest;
import com.example.atm.account.dto.AccountRequest;
import com.example.atm.account.dto.AccountResponse;
import com.example.atm.account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private AccountService accountService;

  // ---------------------------------------------------------
  // POST /account
  // ---------------------------------------------------------

  @Test
  void createAccount_shouldReturnAccount_whenAccountDoesNotExist() throws Exception {

    AccountRequest request = new AccountRequest();
    request.setUserId("user-123");

    AccountResponse response = new AccountResponse();

    when(accountService.getAccountByUserId("user-123")).thenReturn(null);

    when(accountService.createAccount(any(AccountRequest.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("OK003"))
        .andExpect(jsonPath("$.message").value("Account Created Successfully"));
  }

  @Test
  void createAccount_shouldReturnExistingAccount_whenAccountAlreadyExists() throws Exception {

    AccountRequest request = new AccountRequest();
    request.setUserId("user-123");

    AccountResponse response = new AccountResponse();

    when(accountService.getAccountByUserId("user-123")).thenReturn(response);

    mockMvc
        .perform(
            post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("OK003"));

    // createAccount should NOT be called because account already exists.
  }

  // ---------------------------------------------------------
  // GET /account/{id}
  // ---------------------------------------------------------

  @Test
  void getAccountById_shouldReturnAccount() throws Exception {

    AccountResponse response = new AccountResponse();

    when(accountService.getAccountById("account-123")).thenReturn(response);

    mockMvc
        .perform(get("/account/account-123"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("OK999"))
        .andExpect(jsonPath("$.status").value("SUCCESS"));
  }

  // ---------------------------------------------------------
  // GET /account/user/{userId}
  // ---------------------------------------------------------

  @Test
  void getAccountByUserId_shouldReturnAccount() throws Exception {

    AccountResponse response = new AccountResponse();

    when(accountService.getAccountByUserId("user-123")).thenReturn(response);

    mockMvc
        .perform(get("/account/user/user-123"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("OK999"))
        .andExpect(jsonPath("$.status").value("SUCCESS"));
  }

  // ---------------------------------------------------------
  // PUT /account/deposit
  // ---------------------------------------------------------

  @Test
  void deposit_shouldReturnUpdatedAccount() throws Exception {

    AccountOperationRequest request = new AccountOperationRequest();
    request.setId("account-123");
    request.setBalance(new BigDecimal("500"));

    AccountResponse response = new AccountResponse();

    when(accountService.credit(any(AccountOperationRequest.class))).thenReturn(response);

    mockMvc
        .perform(
            put("/account/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("OK004"))
        .andExpect(jsonPath("$.status").value("SUCCESS"));
  }

  // ---------------------------------------------------------
  // PUT /account/withdraw
  // ---------------------------------------------------------

  @Test
  void withdraw_shouldReturnUpdatedAccount() throws Exception {

    AccountOperationRequest request = new AccountOperationRequest();
    request.setId("account-123");
    request.setBalance(new BigDecimal("200"));

    AccountResponse response = new AccountResponse();

    when(accountService.debit(any(AccountOperationRequest.class))).thenReturn(response);

    mockMvc
        .perform(
            put("/account/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("OK005"))
        .andExpect(jsonPath("$.status").value("SUCCESS"));
  }
}
