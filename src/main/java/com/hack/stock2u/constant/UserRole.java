package com.hack.stock2u.constant;

import lombok.Getter;

@Getter
public enum UserRole {
  GENERAL("general"),
  SELLER("seller"),
  ADMIN("admin");

  private final String name;

  UserRole(String name) {
    this.name = name;
  }

}
