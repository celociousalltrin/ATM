package com.example.atm.common.response;

import java.util.Arrays;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseCode {
  // SUCCESS
  SUCCESS(CONSTANTS.OK999, CONSTANTS.OK999_TEXT, HttpStatus.OK),
  USER_LOG_IN(CONSTANTS.OK001, CONSTANTS.OK001_TEXT, HttpStatus.OK),
  USER_LOG_OUT(CONSTANTS.OK002, CONSTANTS.OK002_TEXT, HttpStatus.OK),
  ACCOUNT_CREATED(CONSTANTS.OK003, CONSTANTS.OK003_TEXT, HttpStatus.CREATED),
  AMOUNT_DEPOSITED(CONSTANTS.OK004, CONSTANTS.OK004_TEXT, HttpStatus.OK),
  AMOUNT_WITHDRAW(CONSTANTS.OK005, CONSTANTS.OK005_TEXT, HttpStatus.OK),
  TRANSACTION_CREATED(CONSTANTS.OK006, CONSTANTS.OK006_TEXT, HttpStatus.CREATED),
  DEBT_CREATED(CONSTANTS.OK007, CONSTANTS.OK007_TEXT, HttpStatus.CREATED),
  DEBT_UPDATED(CONSTANTS.OK008, CONSTANTS.OK008_TEXT, HttpStatus.OK),

  // ERROR
  SOMETHING_WENT_WRONG(CONSTANTS.ER999, CONSTANTS.ER999_TEXT, HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_REQUEST_BODY(CONSTANTS.ER997, CONSTANTS.ER997_TEXT, HttpStatus.BAD_REQUEST),
  ENDPOINT_NOT_FOUND(CONSTANTS.ER996, CONSTANTS.ER996_TEXT, HttpStatus.NOT_FOUND),
  INVALID_USER(CONSTANTS.ER001, CONSTANTS.ER001_TEXT, HttpStatus.BAD_REQUEST),
  USER_NAME_NOT_FOUND(CONSTANTS.ER002, CONSTANTS.ER002_TEXT, HttpStatus.NOT_FOUND),
  USER_ID_NOT_FOUND(CONSTANTS.ER003, CONSTANTS.ER003_TEXT, HttpStatus.NOT_FOUND),
  ACCOUNT_NOT_FOUND(CONSTANTS.ER004, CONSTANTS.ER004_TEXT, HttpStatus.NOT_FOUND),
  INVALID_TRANSACTION_TYPE(CONSTANTS.ER005, CONSTANTS.ER005_TEXT, HttpStatus.BAD_REQUEST),
  INVALID_ACCOUNT_ID(CONSTANTS.ER006, CONSTANTS.ER006_TEXT, HttpStatus.BAD_REQUEST),
  SAME_ACCOUNT_TRANSFER_NOT_ALLOWED(
      CONSTANTS.ER007, CONSTANTS.ER007_TEXT, HttpStatus.UNPROCESSABLE_ENTITY),
  INSUFFICIENT_BALANCE(CONSTANTS.ER008, CONSTANTS.ER008_TEXT, HttpStatus.UNPROCESSABLE_ENTITY),
  DEBT_NOT_FOUND(CONSTANTS.ER009, CONSTANTS.ER009_TEXT, HttpStatus.UNPROCESSABLE_ENTITY),
  TRANSACTION_AMOUNT_NOT_EQUAL_TO_DEBT_AMOUNT(
      CONSTANTS.ER010, CONSTANTS.ER010_TEXT, HttpStatus.UNPROCESSABLE_ENTITY),
  DEBT_ALREADY_PAID(CONSTANTS.ER011, CONSTANTS.ER011_TEXT, HttpStatus.UNPROCESSABLE_ENTITY),
  VALIDATION_ERR(CONSTANTS.ER998, CONSTANTS.ER998_TEXT, HttpStatus.UNPROCESSABLE_ENTITY);

  private final String code;
  private final String message;
  private final HttpStatus status;

  ResponseCode(String code, String msg, HttpStatus status) {
    this.message = msg;
    this.status = status;
    this.code = code;
  }

  public static ResponseCode fromCode(String code) {
    return Arrays.stream(ResponseCode.values())
        .filter(rc -> rc.getCode().equals(code))
        .findFirst()
        .orElseGet(() -> ResponseCode.SOMETHING_WENT_WRONG);
  }

  public FormattedResponse format(Object... args) {
    String formattedMsg =
        (args != null && args.length > 0) ? String.format(this.message, args) : this.message;
    return new FormattedResponse(this.code, formattedMsg, this.status);
  }

  public record FormattedResponse(String code, String message, HttpStatus status) {}

  public static class CONSTANTS {
    // SUCCESS CODE
    public static final String OK999 = "OK999";
    public static final String OK001 = "OK001";
    public static final String OK002 = "OK002";
    public static final String OK003 = "OK003";
    public static final String OK004 = "OK004";
    public static final String OK005 = "OK005";
    public static final String OK006 = "OK006";
    public static final String OK007 = "OK007";
    public static final String OK008 = "OK008";

    // ERROR CODE
    public static final String ER999 = "ER999";
    public static final String ER998 = "ER998";
    public static final String ER997 = "ER997";
    public static final String ER996 = "ER996";
    public static final String ER001 = "ER001";
    public static final String ER002 = "ER002";
    public static final String ER003 = "ER003";
    public static final String ER004 = "ER004";
    public static final String ER005 = "ER005";
    public static final String ER006 = "ER006";
    public static final String ER007 = "ER007";
    public static final String ER008 = "ER008";
    public static final String ER009 = "ER009";
    public static final String ER010 = "ER010";
    public static final String ER011 = "ER011";

    // SUCCESS MSG
    public static final String OK999_TEXT = "Request successful";
    public static final String OK001_TEXT = "User Logged in Successfully";
    public static final String OK002_TEXT = "User Logout Successfully";
    public static final String OK003_TEXT = "Account Created Successfully";
    public static final String OK004_TEXT = "Amount has been Deposited Successfully";
    public static final String OK005_TEXT = "Amount has been withdraw Successfully";
    public static final String OK006_TEXT = "Transaction Created Successfully";
    public static final String OK007_TEXT = "Debt Created Successfully";
    public static final String OK008_TEXT = "Debt Updated Successfully";

    // ERROR Message
    public static final String ER999_TEXT = "Something went wrong.Please try again latter.";
    public static final String ER998_TEXT = "Validation Errors";
    public static final String ER997_TEXT = "Invalid request body";
    public static final String ER996_TEXT =
        "The endpoint is not available. Please correct the URL.";
    public static final String ER001_TEXT = "User Name needs a valid value";
    public static final String ER002_TEXT = "user name is not found.Please enter valid name";
    public static final String ER003_TEXT = "User id us not Found.Please provide valid id.";
    public static final String ER004_TEXT = "Account not Exist";
    public static final String ER005_TEXT = "Transaction type is required and must be valid";
    public static final String ER006_TEXT = "Account Id needs to be  valid";
    public static final String ER007_TEXT = "Same Account Transaction is Not Allowed";
    public static final String ER008_TEXT =
        "Your Account Does not have sufficient Balance to Perform this action.";
    public static final String ER009_TEXT = "Debt not Exist.";
    public static final String ER010_TEXT =
        "Transaction Amount is not equal with the Debt Amount.Please enter a valid amount.";
    public static final String ER011_TEXT = "Debt Amount is already Payed";
  }
}
