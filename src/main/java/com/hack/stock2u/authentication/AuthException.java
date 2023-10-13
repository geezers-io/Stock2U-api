package com.hack.stock2u.authentication;

import com.hack.stock2u.global.exception.BasicErrorCase;
import com.hack.stock2u.global.exception.BasicErrorResponse;
import com.hack.stock2u.global.exception.BasicException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthException implements BasicErrorCase {
  EXPIRED_AUTH_CODE(HttpStatus.UNAUTHORIZED, "EXPIRED_AUTH_CODE", "인증코드 유효기간이 만료되었거나 존재하지 않습니다."),
  MISMATCH_AUTH_CODE(HttpStatus.BAD_REQUEST, "MISMATCH_AUTH_CODE", "인증코드가 일치하지 않습니다"),
  ALREADY_PASS_AUTH_CODE(HttpStatus.BAD_REQUEST, "ALREADY_PASS", "이미 검증 완료된 사용자입니다.");

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;

  AuthException(HttpStatus httpStatus, String errorCode, String message) {
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
