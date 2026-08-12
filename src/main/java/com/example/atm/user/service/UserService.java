package com.example.atm.user.service;

import com.example.atm.common.exception.ResourceNotFoundException;
import com.example.atm.common.response.ResponseCode;
import com.example.atm.user.dto.UserRequest;
import com.example.atm.user.dto.UserResponse;
import com.example.atm.user.entity.User;
import com.example.atm.user.mapper.UserMapper;
import com.example.atm.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserResponse getOrCreate(UserRequest payload) {
    User user =
        this.userRepository
            .findByUserName(payload.getUserName())
            .orElseGet(
                () -> {
                  User createdUser = this.userMapper.toEntity(payload);
                  return this.userRepository.save(createdUser);
                });

    return this.userMapper.toResponse(user);
  }

  public UserResponse getUser(String id) {
    User user =
        this.userRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(ResponseCode.USER_NAME_NOT_FOUND));
    return this.userMapper.toResponse(user);
  }

  public void logoutUser(String id) {
    boolean isUserPresent = this.userRepository.isPresentById(id);
    if (!isUserPresent) {
      throw new ResourceNotFoundException(ResponseCode.USER_ID_NOT_FOUND);
    }
  }
}
