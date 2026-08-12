package com.example.atm.common.exception;

import com.example.atm.common.exception.handler.AppException;
import com.example.atm.common.response.ResponseCode;

public class BusinessException extends AppException {
  public BusinessException(ResponseCode responseCode) {
    super(responseCode);
  }
}
