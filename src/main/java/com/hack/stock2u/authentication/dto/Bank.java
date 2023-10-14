package com.hack.stock2u.authentication.dto;

import com.hack.stock2u.constant.BankCode;

public record Bank(String code, String name) {
  public static Bank from(BankCode bankCode) {
    return new Bank(bankCode.getCode(), bankCode.getName());
  }
}
