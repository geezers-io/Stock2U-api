package com.hack.stock2u.constant;

public enum MessageTemplate {
  RESERVATION_REQUEST("""
  "{0}" 재고 예약이 요청되었습니다
  """),
  RESERVATION_CANCEL("""
  "{0}" 요청 취소가 완료되었습니다.
  """),
  RESERVATION_APPORVE("""
  "{0}" 요청이 승인되었습니다.
  """),
  RESERVATION_SUCCESS("""
  "{0}" 구매가 확정되었습니다.
  """);

  private final String message;

  MessageTemplate(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}
