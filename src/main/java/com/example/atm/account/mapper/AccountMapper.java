package com.example.atm.account.mapper;

import com.example.atm.account.dto.AccountRequest;
import com.example.atm.account.dto.AccountResponse;
import com.example.atm.account.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
  Account toEntity(AccountRequest accountRequest);

  AccountResponse toResponse(Account account);
}
