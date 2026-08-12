package com.example.atm.debt.mapper;

import com.example.atm.debt.dto.DebtPayableResponse;
import com.example.atm.debt.dto.DebtRequest;
import com.example.atm.debt.dto.DebtResponse;
import com.example.atm.debt.entity.Debt;
import com.example.atm.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DebtMapper {
  Debt toEntity(DebtRequest debtRequest);

  DebtResponse toResponse(Debt debt);

  @Mapping(source = "debt.id", target = "debtId")
  @Mapping(source = "user.userName", target = "userName")
  @Mapping(source = "debt.createdAt", target = "createdAt")
  DebtPayableResponse toDebtPayableResponse(Debt debt, User user);
}
