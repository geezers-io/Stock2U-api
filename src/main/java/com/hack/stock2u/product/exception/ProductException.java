package com.hack.stock2u.product.exception;

import com.hack.stock2u.global.exception.BasicErrorCase;
import com.hack.stock2u.global.exception.BasicErrorResponse;
import com.hack.stock2u.global.exception.BasicException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProductException implements BasicErrorCase {
  FILE_UPLOAD_LIMIT(HttpStatus.BAD_REQUEST, "FILE_UPLOAD_LIMIT", "이미지는 업로드는 최대 5개입니다.");

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;

  ProductException(HttpStatus httpStatus, String errorCode, String message) {
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
