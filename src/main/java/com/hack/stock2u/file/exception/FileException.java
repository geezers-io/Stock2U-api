package com.hack.stock2u.file.exception;

import com.hack.stock2u.global.exception.BasicErrorCase;
import com.hack.stock2u.global.exception.BasicErrorResponse;
import com.hack.stock2u.global.exception.BasicException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FileException implements BasicErrorCase {
  FILE_NOT_IMAGE(HttpStatus.BAD_REQUEST, "FILE_NOT_IMAGE", "이미지 확장자 파일이 아닙니다."),
  NOT_EXT(HttpStatus.BAD_REQUEST, "NOT_FILE_EXTENSION", "파일 확장자가 존재하지 않습니다."),
  NOT_INCLUDE_FILE(HttpStatus.BAD_REQUEST, "NOT_INCLUDE_FILE", "업로드할 파일이 없습니다.");

  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String message;

  FileException(HttpStatus httpStatus, String errorCode, String message) {
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
