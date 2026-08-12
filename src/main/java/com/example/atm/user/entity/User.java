package com.example.atm.user.entity;

import com.example.atm.common.util.AppUtil;
import java.time.Instant;
import lombok.Getter;

@Getter
public class User {
  private final String id = AppUtil.generateUUID();
  private final String userName;
  private final Instant createdAt = Instant.now();

  public User(String userName) {
    this.userName = userName;
  }
}
