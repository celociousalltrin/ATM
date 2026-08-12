package com.example.atm.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.atm.common.exception.ResourceNotFoundException;
import com.example.atm.user.dto.UserRequest;
import com.example.atm.user.dto.UserResponse;
import com.example.atm.user.entity.User;
import com.example.atm.user.mapper.UserMapper;
import com.example.atm.user.repository.UserRepository;
import com.example.atm.user.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private UserMapper userMapper;

  @InjectMocks private UserService userService;

  private User user;
  private UserRequest userRequest;
  private UserResponse userResponse;

  @BeforeEach
  void setUp() {
    user = new User("john");

    userRequest = new UserRequest();
    userRequest.setUserName("john");

    userResponse = new UserResponse();
  }

  // ---------------------------------------------------------
  // getOrCreate()
  // ---------------------------------------------------------

  @Test
  void getOrCreate_shouldReturnExistingUser_whenUserAlreadyExists() {

    when(userRepository.findByUserName("john")).thenReturn(Optional.of(user));

    when(userMapper.toResponse(user)).thenReturn(userResponse);

    UserResponse result = userService.getOrCreate(userRequest);

    assertNotNull(result);
    assertSame(userResponse, result);

    verify(userRepository).findByUserName("john");

    // Existing user → should NOT create/save another user
    verify(userRepository, never()).save(any(User.class));

    verify(userMapper).toResponse(user);
  }

  @Test
  void getOrCreate_shouldCreateUser_whenUserDoesNotExist() {

    User newUser = new User("john");

    when(userRepository.findByUserName("john")).thenReturn(Optional.empty());

    when(userMapper.toEntity(userRequest)).thenReturn(newUser);

    when(userRepository.save(newUser)).thenReturn(newUser);

    when(userMapper.toResponse(newUser)).thenReturn(userResponse);

    UserResponse result = userService.getOrCreate(userRequest);

    assertNotNull(result);
    assertSame(userResponse, result);

    verify(userRepository).findByUserName("john");

    verify(userMapper).toEntity(userRequest);

    verify(userRepository).save(newUser);

    verify(userMapper).toResponse(newUser);
  }

  // ---------------------------------------------------------
  // getUser()
  // ---------------------------------------------------------

  @Test
  void getUser_shouldReturnUser_whenUserExists() {

    String userId = user.getId();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    when(userMapper.toResponse(user)).thenReturn(userResponse);

    UserResponse result = userService.getUser(userId);

    assertNotNull(result);
    assertSame(userResponse, result);

    verify(userRepository).findById(userId);
    verify(userMapper).toResponse(user);
  }

  @Test
  void getUser_shouldThrowException_whenUserDoesNotExist() {

    String userId = "invalid-id";

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> userService.getUser(userId));

    verify(userRepository).findById(userId);

    // Mapper should never be called
    verify(userMapper, never()).toResponse(any(User.class));
  }

  // ---------------------------------------------------------
  // logoutUser()
  // ---------------------------------------------------------

  @Test
  void logoutUser_shouldCompleteSuccessfully_whenUserExists() {

    String userId = user.getId();

    when(userRepository.isPresentById(userId)).thenReturn(true);

    assertDoesNotThrow(() -> userService.logoutUser(userId));

    verify(userRepository).isPresentById(userId);
  }

  @Test
  void logoutUser_shouldThrowException_whenUserDoesNotExist() {

    String userId = "invalid-id";

    when(userRepository.isPresentById(userId)).thenReturn(false);

    assertThrows(ResourceNotFoundException.class, () -> userService.logoutUser(userId));

    verify(userRepository).isPresentById(userId);
  }
}
