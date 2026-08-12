package com.example.atm.debt.repository;

import com.example.atm.debt.entity.Debt;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class DebtRepository {
  private final Map<String, Debt> debts = new ConcurrentHashMap<>();

  public Debt save(Debt entity) {
    this.debts.put(entity.getId(), entity);
    return entity;
  }

  public Debt update(String id, Debt entity) {
    this.debts.put(id, entity);
    return this.debts.get(id);
  }

  public Optional<Debt> findById(String id) {
    return Optional.ofNullable(this.debts.get(id));
  }

  public List<Debt> findByOwedBy(String accountId) {
    System.out.println(this.debts);
    return this.debts.values().stream().filter(debt -> debt.getOwedBy().equals(accountId)).toList();
  }

  public List<Debt> findByOwedTo(String accountId) {
    return this.debts.values().stream().filter(debt -> debt.getOwedTo().equals(accountId)).toList();
  }

  public Optional<Debt> findByOwedByAndOwedToID(String owedById, String owedToId) {
    return this.debts.values().stream()
        .filter(debt -> debt.getOwedBy().equals(owedById) && debt.getOwedTo().equals(owedToId))
        .findFirst();
  }
}
