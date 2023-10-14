package com.hack.stock2u.authentication.service.strategy;

import com.hack.stock2u.authentication.config.KakaoProperties;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@RequiredArgsConstructor
public class KakaoURLStrategy implements SignInUrlCreateStrategy {
  private final KakaoProperties properties;

  @Override
  public String create() {
    return String.format(urlTemplate, properties.getClientId(), properties.getRedirectUri());
  }
}
