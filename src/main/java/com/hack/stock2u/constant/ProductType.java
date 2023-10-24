package com.hack.stock2u.constant;

import java.util.Arrays;
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

  public static ProductType findByName(String name) {
    return Arrays.stream(ProductType.values())
        .filter(vendor -> {
          String vendorName = vendor.name();
          return vendorName.equals(name);
        })
        .findAny()
        .orElseThrow(() -> new IllegalArgumentException("vendor 요청이 잘못되었습니다."));
  }

}
