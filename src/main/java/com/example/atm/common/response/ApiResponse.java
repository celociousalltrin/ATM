package com.example.atm.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
  private final String code;
  private final String message;
  private final T responseData;
  private final ResponseStatus status;
  private final List<FieldErrorDetails> errors;

  @JsonProperty("isBulkErrors")
  private final Boolean isBulkErrors;

  public static <T> ApiResponse<T> success(ResponseCode responseCode, T respData) {
    return ApiResponse.<T>builder()
        .code(responseCode.getCode())
        .message(responseCode.getMessage())
        .responseData(respData)
        .status(ResponseStatus.SUCCESS)
        .build();
  }

  public static <T> ApiResponse<T> error(ResponseCode responseCode) {
    return ApiResponse.<T>builder()
        .code(responseCode.getCode())
        .message(responseCode.getMessage())
        .status(ResponseStatus.ERROR)
        .isBulkErrors(false)
        .build();
  }

  public static <T> ApiResponse<T> error(
      ResponseCode responseCode, List<FieldErrorDetails> errors) {
    return ApiResponse.<T>builder()
        .code(responseCode.getCode())
        .message(responseCode.getMessage())
        .status(ResponseStatus.ERROR)
        .errors(errors)
        .isBulkErrors(true)
        .build();
  }
}

enum ResponseStatus {
  SUCCESS,
  ERROR
}
