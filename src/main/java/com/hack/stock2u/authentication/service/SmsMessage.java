package com.hack.stock2u.authentication.service;

import lombok.Getter;

@Getter
public enum SmsMessage {
  SEND_CODE("""
  [Stock2U]
  회원가입 사용자 인증코드를 발송하였습니다.
  인증코드: {0}
  5분 이후 해당 코드는 만료되니 주의바랍니다.
  """);

  private final String message;

  SmsMessage(String message) {
    this.message = message;
  }

}
