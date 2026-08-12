package com.example.atm.user.mapper;

import com.example.atm.user.dto.UserRequest;
import com.example.atm.user.dto.UserResponse;
import com.example.atm.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toEntity(UserRequest user);

  UserResponse toResponse(User user);
}
