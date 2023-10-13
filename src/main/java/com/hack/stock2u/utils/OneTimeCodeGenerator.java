package com.hack.stock2u.utils;

import java.util.Random;

public class OneTimeCodeGenerator {

  /**
   * 지정된 자리의 숫자로 구성된 랜덤코드를 생성합니다.
   */
  public static String createDigit(int len) {
    Random r = new Random();
    StringBuilder sb = new StringBuilder(len);

    for (int i = 0; i < len; i++) {
      int n = r.nextInt(10);
      sb.append(n);
    }

    return sb.toString();
  }

}
