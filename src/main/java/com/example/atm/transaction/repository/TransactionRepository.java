package com.example.atm.transaction.repository;

import com.example.atm.transaction.entity.Transaction;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class TransactionRepository {
  private final Map<String, Transaction> transactions = new ConcurrentHashMap<>();

  public Transaction save(Transaction transaction) {
    this.transactions.put(transaction.getId(), transaction);
    return transaction;
  }

  public Optional<Transaction> findById(String id) {
    return Optional.ofNullable(this.transactions.get(id));
  }

  public Optional<Transaction> findByAccountId(String id) {
    return this.transactions.values().stream()
        .filter(tr -> tr.getAccountId().equals(id))
        .findFirst();
  }
}
