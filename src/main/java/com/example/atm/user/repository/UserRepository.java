package com.example.atm.user.repository;

import com.example.atm.user.entity.User;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class UserRepository {
  private final Map<String, User> users = new ConcurrentHashMap<>();

  public UserRepository() {}

  public User save(User entity) {
    this.users.put(entity.getId(), entity);
    return entity;
  }

  public Optional<User> findById(String id) {
    return Optional.ofNullable(this.users.get(id));
  }

  public Optional<User> findByUserName(String userName) {
    return this.users.values().stream()
        .filter(user -> user.getUserName().equals(userName))
        .findFirst();
  }

  public boolean isPresentById(String id) {
    User user = this.users.get(id);
    return user != null;
  }

  public boolean isPresentByUserName(String name) {
    return this.users.values().stream().anyMatch(user -> user.getUserName().equals(name));
  }
}
