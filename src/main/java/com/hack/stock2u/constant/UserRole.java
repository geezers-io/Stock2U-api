package com.hack.stock2u.constant;

import lombok.Getter;

@Getter
public enum UserRole {
  GENERAL("구매자"),
  SELLER("판매자"),
  ADMIN("관리자");

  private final String name;

  UserRole(String name) {
    this.name = name;
  }

}
