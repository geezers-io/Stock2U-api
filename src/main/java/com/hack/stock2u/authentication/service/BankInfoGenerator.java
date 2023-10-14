package com.hack.stock2u.authentication.service;

import java.util.HashMap;
import java.util.Map;

public class BankInfoGenerator {
  public Map<String, String> generateCardVendor() {
    HashMap<String, String> map = new HashMap<>();

    map.put("01", "비씨");
    map.put("02", "KB국민");
    map.put("03", "하나(외환)");
    map.put("04", "삼성");
    map.put("06", "신한");
    map.put("07", "현대");
    map.put("08", "롯데");
    map.put("32", "우체국");
    map.put("33", "저축은행");
    map.put("34", "은련");
    map.put("35", "새마을금고");
    map.put("36", "KDB산업");
    map.put("37", "카카오뱅크");
    map.put("38", "케이뱅크");
    map.put("39", "PAYCO 포인트");
    map.put("40", "카카오머니");
    map.put("41", "SSG머니");
    map.put("42", "네이버페이포인트");
    map.put("09", "한미");
    map.put("10", "신세계한미");
    map.put("11", "씨티");
    map.put("12", "NH채움");
    map.put("13", "수협");
    map.put("15", "우리 BD");
    map.put("16", "하나");
    map.put("17", "우리");
    map.put("21", "광주");
    map.put("22", "전북");
    map.put("23", "제주");
    map.put("24", "산은캐피탈");
    map.put("25", "해외비자");
    map.put("26", "해외마스터");
    map.put("27", "해외다이너스");
    map.put("28", "해외AMX");
    map.put("29", "해외JCB");
    map.put("31", "SK-OKCashBag");

    return map;
  }

}
