package com.hack.stock2u.constant;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import lombok.Getter;

@Schema
@Getter
public enum AuthVendor {
  GOOGLE("구글"),
  KAKAO("카카오"),
  NAVER("네이버");

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
          String vendorName = vendor.name();
          return vendorName.equals(name);
        })
        .findAny()
        .orElseThrow(() -> new IllegalArgumentException("vendor 요청이 잘못되었습니다."));
  }

}
