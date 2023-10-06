package com.hack.stock2u.authentication.service.strategy;

import com.hack.stock2u.authentication.config.KakaoProperties;
import com.hack.stock2u.constant.AuthVendor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LoginStrategyBranch {
  private final KakaoProperties kakaoProperties;

  /**
   * 벤더 전략 클래스(LoginURLCreateStrategy) 에 따른 URL 생성 및 반환을 수행합니다.
   */
  public LoginURLCreateStrategy getStrategy(AuthVendor authVendor) {
    if (authVendor.equals(AuthVendor.KAKAO)) {
      return new KakaoURLStrategy(kakaoProperties);
    }
    throw new IllegalArgumentException("잘못된 Vendor 값입니다.");
  }

}
