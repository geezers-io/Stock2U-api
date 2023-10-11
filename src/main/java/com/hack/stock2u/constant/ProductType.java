package com.hack.stock2u.constant;

import lombok.Getter;

@Getter
public enum ProductType {
  FOOD("식품"),
  ACCOMMODATION("숙박"),
  TICKET("티켓");

  private final String name;

  ProductType(String name) {
    this.name = name;
  }

}
