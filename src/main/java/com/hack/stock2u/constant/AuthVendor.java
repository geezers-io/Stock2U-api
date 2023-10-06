package com.hack.stock2u.constant;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum AuthVendor {
  GOOGLE("google"),
  KAKAO("kakao"),
  NAVER("naver");

  private final String name;

  AuthVendor(String name) {
    this.name = name;
  }

  /**
   * @param name kakao, naver, google 각 로그인 벤더사 이름
   * @return AuthVendor
   */
  public static AuthVendor findByName(String name) {
    return Arrays.stream(AuthVendor.values())
        .filter(vendor -> {
          String vendorName = vendor.getName();
          return vendorName.equals(name);
        })
        .findAny()
        .orElseThrow(IllegalArgumentException::new);
  }

}
