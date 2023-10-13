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
  ALREADY_PASS_AUTH_CODE(HttpStatus.BAD_REQUEST, "ALREADY_PASS", "이미 검증 완료된 사용자입니다."),

  DORO_TOO_LONG(HttpStatus.BAD_REQUEST, "TOO_LONG_SEARCH", "검색어가 너무 깁니다. (한글40자, 영문,숫자 80자 이하)"),
  DORO_CHARACTER(HttpStatus.BAD_REQUEST, "BAD_CHARACTER", "검색어는 문자와 숫자 같이 입력되어야 합니다. (숫자만 검색 불가)"),
  DORO_ONLY_ONE_CHAR(HttpStatus.BAD_REQUEST, "ONLY_ONE_CHAR", "검색어는 한글자 이상 입력되어야 합니다."),
  DORO_NOT_INPUT(HttpStatus.BAD_REQUEST, "NOT_INPUT", "검색어가 입력되지 않았습니다."),
  DORO_OVER_SEARCHING(HttpStatus.BAD_REQUEST, "OVERSEARCHING", "검색 범위를 초과하였습니다."),
  DORO_BAD_FORM(HttpStatus.BAD_REQUEST, "BAD_FORM", "특수문자+숫자만으로는 검색이 불가능 합니다."),
  DORO_KEY_EXPIRED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "키 기간이 만료되어 서비스를 이용하실 수 없습니다.");

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
