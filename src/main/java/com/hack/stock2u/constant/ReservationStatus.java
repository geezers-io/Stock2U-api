package com.hack.stock2u.constant;

import lombok.Getter;

@Getter
public enum ReservationStatus {
  PROGRESS("예약 진행중"),
  CANCEL("예약 취소됨"),
  COMPLETION("구매 완료됨");

  private final String name;

  ReservationStatus(String name) {
    this.name = name;
  }

}
