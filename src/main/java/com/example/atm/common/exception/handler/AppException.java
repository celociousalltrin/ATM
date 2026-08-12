package com.example.atm.common.exception.handler;

import com.example.atm.common.response.ResponseCode;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
  private final ResponseCode responseCode;

  public AppException(ResponseCode responseCode) {
    super(responseCode.getMessage());
    this.responseCode = responseCode;
  }
}
