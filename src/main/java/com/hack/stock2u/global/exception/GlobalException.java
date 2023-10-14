package com.hack.stock2u.global.exception;

import org.springframework.http.HttpStatus;

public enum GlobalException implements BasicErrorCase {
  ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "FAULT_ARG", "잘못된 인자 값입니다."),
  SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_ERROR", "서버 에러가 발생하였습니다."),
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "잘못된 요청입니다.");

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;

  GlobalException(HttpStatus httpStatus, String errorCode, String message) {
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
