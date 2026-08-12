package com.example.atm.common.exception.handler;

import com.example.atm.common.response.ApiResponse;
import com.example.atm.common.response.FieldErrorDetails;
import com.example.atm.common.response.ResponseCode;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AppException.class)
  public ResponseEntity<ApiResponse<?>> handleAppException(AppException ex) {
    ResponseCode responseCode = ex.getResponseCode();
    log.warn(
        "Application exception | code={} message={}",
        responseCode.getCode(),
        responseCode.getMessage());
    ApiResponse<?> response = ApiResponse.error(responseCode);

    return ResponseEntity.status(responseCode.getStatus()).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<?>> handleValidationException(
      MethodArgumentNotValidException ex) {

    List<FieldErrorDetails> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                fieldError ->
                    new FieldErrorDetails(
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        ResponseCode.fromCode(fieldError.getDefaultMessage()).getMessage()))
            .toList();
    log.warn("Validation failed | fields={}", errors.size());
    ApiResponse<?> response = ApiResponse.error(ResponseCode.VALIDATION_ERR, errors);
    return ResponseEntity.status(ResponseCode.VALIDATION_ERR.getStatus()).body(response);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<?>> handleJacksonError(HttpMessageNotReadableException ex) {
    log.warn("Invalid request body | message={}", ex.getMessage());
    ApiResponse<?> response = ApiResponse.error(ResponseCode.INVALID_REQUEST_BODY);

    return ResponseEntity.status(ResponseCode.INVALID_REQUEST_BODY.getStatus()).body(response);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ApiResponse<?>> handleEndPointNotFoundException(
      NoResourceFoundException ex) {
    log.warn("Endpoint not found | resource={}", ex.getResourcePath());
    ApiResponse<?> response = ApiResponse.error(ResponseCode.ENDPOINT_NOT_FOUND);
    return ResponseEntity.status(ResponseCode.ENDPOINT_NOT_FOUND.getStatus()).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<?>> handleException(Exception ex) {
    log.error("Unexpected application exception", ex);
    ApiResponse<?> response = ApiResponse.error(ResponseCode.SOMETHING_WENT_WRONG);
    return ResponseEntity.status(ResponseCode.SOMETHING_WENT_WRONG.getStatus()).body(response);
  }
}
