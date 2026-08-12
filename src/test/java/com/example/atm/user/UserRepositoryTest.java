package com.example.atm.user;

import static org.junit.jupiter.api.Assertions.*;

import com.example.atm.user.entity.User;
import com.example.atm.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserRepositoryTest {

  private UserRepository userRepository;

  private User user;

  @BeforeEach
  void setUp() {
    userRepository = new UserRepository();
    user = new User("john");
  }

  @Test
  void save_shouldStoreAndReturnUser() {

    User result = userRepository.save(user);

    assertSame(user, result);
  }

  @Test
  void findById_shouldReturnUser_whenUserExists() {

    userRepository.save(user);

    Optional<User> result = userRepository.findById(user.getId());

    assertTrue(result.isPresent());
    assertSame(user, result.get());
  }

  @Test
  void findById_shouldReturnEmpty_whenUserDoesNotExist() {

    Optional<User> result = userRepository.findById("invalid-id");

    assertTrue(result.isEmpty());
  }

  @Test
  void findByUserName_shouldReturnUser_whenUserExists() {

    userRepository.save(user);

    Optional<User> result = userRepository.findByUserName("john");

    assertTrue(result.isPresent());
    assertSame(user, result.get());
  }

  @Test
  void findByUserName_shouldReturnEmpty_whenUserDoesNotExist() {

    Optional<User> result = userRepository.findByUserName("unknown");

    assertTrue(result.isEmpty());
  }

  @Test
  void isPresentById_shouldReturnTrue_whenUserExists() {

    userRepository.save(user);

    assertTrue(userRepository.isPresentById(user.getId()));
  }

  @Test
  void isPresentById_shouldReturnFalse_whenUserDoesNotExist() {

    assertFalse(userRepository.isPresentById("invalid-id"));
  }

  @Test
  void isPresentByUserName_shouldReturnTrue_whenUserExists() {

    userRepository.save(user);

    assertTrue(userRepository.isPresentByUserName("john"));
  }

  @Test
  void isPresentByUserName_shouldReturnFalse_whenUserDoesNotExist() {

    assertFalse(userRepository.isPresentByUserName("unknown"));
  }
}
