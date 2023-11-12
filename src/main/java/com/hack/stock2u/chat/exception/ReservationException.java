package com.hack.stock2u.chat.exception;

import com.hack.stock2u.global.exception.BasicErrorCase;
import com.hack.stock2u.global.exception.BasicErrorResponse;
import com.hack.stock2u.global.exception.BasicException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReservationException implements BasicErrorCase {
  NOT_ENOUGH_COUNT(HttpStatus.BAD_REQUEST, "NOT_ENOUGH_COUNT", "재고 개수가 소진되었습니다."),
  PRODUCT_EXPIRED(HttpStatus.GONE, "PRODUCT_EXPIRED", "상품 기한이 만료되었습니다."),
  ALREADY_EXISTS(HttpStatus.GONE, "RESERVATION_EXISTS", "이미 예약이 요청되었습니다"),
  ALREADY_REPORTED(HttpStatus.GONE, "ALREADY_REPORTED", "이미 신고 하셨습니다.");

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
