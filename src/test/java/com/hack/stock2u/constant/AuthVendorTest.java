package com.hack.stock2u.constant;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthVendorTest {

  @Test
  @DisplayName("google, kakao, naver 가 아닌 벤더로 findByName 시 IllegalArgmuentException 이 발생한다.")
  void failureFindByName() {
    // given
    String name = "github";

    // when, then
    assertThrows(IllegalArgumentException.class, () -> {
      AuthVendor.findByName(name);
    });
  }

}