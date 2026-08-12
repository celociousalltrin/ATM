package com.example.atm.common.exception;

import com.example.atm.common.exception.handler.AppException;
import com.example.atm.common.response.ResponseCode;

public class ResourceNotFoundException extends AppException {
  public ResourceNotFoundException(ResponseCode responseCode) {
    super(responseCode);
  }
}
