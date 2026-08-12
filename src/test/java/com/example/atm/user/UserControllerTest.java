package com.example.atm.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.atm.user.controller.UserController;
import com.example.atm.user.dto.UserRequest;
import com.example.atm.user.dto.UserResponse;
import com.example.atm.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private UserService userService;

  // ---------------------------------------------------------
  // POST /login
  // ---------------------------------------------------------

  @Test
  void login_shouldReturnSuccess_whenRequestIsValid() throws Exception {

    UserRequest request = new UserRequest();
    request.setUserName("john");

    UserResponse response = new UserResponse();

    when(userService.getOrCreate(any(UserRequest.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(userService).getOrCreate(any(UserRequest.class));
  }

  // ---------------------------------------------------------
  // GET /logout/{id}
  // ---------------------------------------------------------

  @Test
  void logout_shouldReturnSuccess_whenUserExists() throws Exception {

    String userId = "user-123";

    doNothing().when(userService).logoutUser(userId);

    mockMvc.perform(get("/logout/{id}", userId)).andExpect(status().isOk());

    verify(userService).logoutUser(userId);
  }

  // ---------------------------------------------------------
  // GET /user/{id}
  // ---------------------------------------------------------

  @Test
  void getUser_shouldReturnSuccess_whenUserExists() throws Exception {

    String userId = "user-123";

    UserResponse response = new UserResponse();

    when(userService.getUser(userId)).thenReturn(response);

    mockMvc.perform(get("/user/{id}", userId)).andExpect(status().isOk());

    verify(userService).getUser(userId);
  }
}
