package com.hack.stock2u.constant;

import lombok.Getter;

@Getter
public enum ProductType {
  FOOD("food"),
  ACCOMMODATION("accommodation"),
  TICKET("ticket");

  private final String name;

  ProductType(String name) {
    this.name = name;
  }

}
