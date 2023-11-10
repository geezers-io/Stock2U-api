package com.hack.stock2u.constant;

import lombok.Getter;


public enum AutoMessageTemplate {
  PURCHASE_REQUEST("[자동 발신 메세지] \n {0} 구매를 원합니다."),
  SALE_APPROVED("[자동 발신 메세지] \n {0} 판매가 예약되었습니다."),
  RESERVATION_CANCELLED("[자동 발신 메세지] \n {0} 예약이 취소되었습니다.");

  private final String template;

  AutoMessageTemplate(String template) {
    this.template = template;
  }

  public String getTemplate() {
    return template;
  }
}
