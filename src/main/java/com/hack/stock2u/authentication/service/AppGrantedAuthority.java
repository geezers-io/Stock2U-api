package com.hack.stock2u.authentication.service;

import com.hack.stock2u.constant.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;

@Slf4j
@RequiredArgsConstructor
public class AppGrantedAuthority implements GrantedAuthority {
  private final UserRole userRole;

  @Override
  public String getAuthority() {
    return userRole.name();
  }
}
