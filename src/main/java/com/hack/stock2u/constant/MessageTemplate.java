package com.hack.stock2u.constant;

public enum MessageTemplate {
  RESERVATION_REQUEST("""
  "{0}" 재고 예약이 요청되었습니다
  """);

  private final String message;

  MessageTemplate(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}
