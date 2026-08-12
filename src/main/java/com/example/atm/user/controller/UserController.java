package com.example.atm.user.controller;

import com.example.atm.common.response.ApiResponse;
import com.example.atm.common.response.ResponseCode;
import com.example.atm.user.dto.UserRequest;
import com.example.atm.user.dto.UserResponse;
import com.example.atm.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping("/login")
  public ApiResponse<UserResponse> login(@Valid @RequestBody UserRequest payload) {
    return ApiResponse.success(ResponseCode.USER_LOG_IN, this.userService.getOrCreate(payload));
  }

  @GetMapping("/logout/{id}")
  public ApiResponse<Void> logout(@PathVariable("id") String id) {
    this.userService.logoutUser(id);
    return ApiResponse.success(ResponseCode.USER_LOG_OUT, null);
  }

  @GetMapping("/user/{id}")
  public ApiResponse<UserResponse> user(@PathVariable("id") String id) {
    return ApiResponse.success(ResponseCode.SUCCESS, this.userService.getUser(id));
  }
}
