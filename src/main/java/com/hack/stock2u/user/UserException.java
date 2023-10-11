package com.hack.stock2u.user;

import com.hack.stock2u.global.exception.BasicErrorCase;
import com.hack.stock2u.global.exception.BasicErrorResponse;
import com.hack.stock2u.global.exception.BasicException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserException implements BasicErrorCase {
  NOT_FOUND_USER(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다");

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;

  UserException(HttpStatus httpStatus, String errorCode, String message) {
    this.httpStatus = httpStatus;
    this.errorCode = errorCode;
    this.message = message;
  }

  @Override
  public BasicException create() {
    BasicErrorResponse response = BasicErrorResponse.builder()
        .httpStatus(httpStatus)
        .errorCode(errorCode)
        .message(message)
        .build();
    return new BasicException(response);
  }

}
