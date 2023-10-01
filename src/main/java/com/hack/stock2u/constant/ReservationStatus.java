package com.hack.stock2u.constant;

import lombok.Getter;

@Getter
public enum ReservationStatus {
  PROGRESS("progress"), // 예약 진행중
  CANCEL("cancel"), // 예약 취소됨
  COMPLETION("completion"); // 구매 완료됨

  private final String name;

  ReservationStatus(String name) {
    this.name = name;
  }

}
