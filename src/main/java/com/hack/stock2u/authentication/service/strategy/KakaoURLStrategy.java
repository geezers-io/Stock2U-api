package com.hack.stock2u.authentication.service.strategy;

import com.hack.stock2u.authentication.config.KakaoProperties;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KakaoUrlStrategy implements LoginUrlCreateStrategy {
  private final KakaoProperties properties;

  @Override
  public String create() {
    return String.format(urlTemplate, properties.getClientId(), properties.getRedirectUri());
  }
}
