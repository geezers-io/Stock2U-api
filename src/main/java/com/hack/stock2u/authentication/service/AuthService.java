package com.hack.stock2u.authentication.service;

import com.hack.stock2u.authentication.dto.UrlJson;
import com.hack.stock2u.authentication.service.strategy.LoginStrategyBranch;
import com.hack.stock2u.authentication.service.strategy.LoginURLCreateStrategy;
import com.hack.stock2u.constant.AuthVendor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

  private final LoginStrategyBranch loginStrategyBranch;

  /**
   * AuthVendor 에 따른 각 Vendor 로그인을 수행하는 URL 을 반환합니다.
   * @param vendor AuthVendor 인증사 벤더 Enum 객체
   */
  public UrlJson getLoginUrl(AuthVendor vendor) {
    LoginURLCreateStrategy urlCreateStrategy = loginStrategyBranch.getStrategy(vendor);
    return new UrlJson(urlCreateStrategy.create());
  }

}
