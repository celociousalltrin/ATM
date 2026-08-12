package com.example.atm.account.repository;

import com.example.atm.account.entity.Account;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class AccountRepository {
  private final Map<String, Account> accounts = new ConcurrentHashMap<>();

  public Account save(Account entity) {
    this.accounts.put(entity.getId(), entity);
    return entity;
  }

  public Optional<Account> findById(String id) {
    return Optional.ofNullable(this.accounts.get(id));
  }

  public boolean isAccountExists(String id) {
    return this.accounts.values().stream().anyMatch(account -> account.getId().equals(id));
  }

  public Optional<Account> findByUserId(String userId) {
    return this.accounts.values().stream()
        .filter(account -> account.getUserId().equals(userId))
        .findFirst();
  }

  public Account update(String id, Account account) {
    this.accounts.put(id, account);
    return this.accounts.get(id);
  }
}
