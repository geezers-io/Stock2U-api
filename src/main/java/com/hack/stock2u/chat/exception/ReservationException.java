package com.hack.stock2u.chat.exception;

import com.hack.stock2u.global.exception.BasicErrorCase;
import com.hack.stock2u.global.exception.BasicErrorResponse;
import com.hack.stock2u.global.exception.BasicException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReservationException implements BasicErrorCase {
  NOT_ENOUGH_COUNT(HttpStatus.BAD_REQUEST, "NOT_ENOUGH_COUNT", "재고 개수가 소진되었습니다.");

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;

  ReservationException(HttpStatus httpStatus, String errorCode, String message) {
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
