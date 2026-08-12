package com.example.atm.transaction.mapper;

import com.example.atm.account.dto.AccountOperationRequest;
import com.example.atm.debt.dto.DebtRequest;
import com.example.atm.transaction.dto.TransactionRequest;
import com.example.atm.transaction.dto.TransactionResponse;
import com.example.atm.transaction.entity.Transaction;
import java.math.BigDecimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
  Transaction toEntity(TransactionRequest transactionRequest);

  TransactionResponse toResponse(Transaction transaction);

  @Mapping(source = "accountId", target = "id")
  @Mapping(source = "amount", target = "balance")
  AccountOperationRequest toAccountOperationRequest(Transaction transaction);

  @Mapping(source = "targetAccountId", target = "id")
  @Mapping(source = "amount", target = "balance")
  AccountOperationRequest toTargetAccountOperationRequest(Transaction transaction);

  DebtRequest toSourceDebtRequest(String owedBy, String owedTo, BigDecimal amount);
}
