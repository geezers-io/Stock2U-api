package com.hack.stock2u.constant;

import lombok.Getter;

@Getter
public enum ChatAlertType {
  CREATION("예약 생성"),
  MESSAGE("메세지"),
  PROGRESS("예약 승인"),
  CANCEL("예약 취소"),
  COMPLETION("구매 완료");

  private final String name;

  ChatAlertType(String name) {
    this.name = name;
  }
}
