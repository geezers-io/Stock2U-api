package com.hack.stock2u.utils;

public class Convertor {

  /**
   * 한국에서 사용하는 폰번호 형식으로 변환된 데이터를 반환합니다. (+82...)
   */
  public String phone(String number) {
    String sub = number.substring(1);
    return "+82" + sub;
  }

}
