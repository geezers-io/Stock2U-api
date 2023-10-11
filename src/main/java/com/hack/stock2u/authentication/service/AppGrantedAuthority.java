package com.hack.stock2u.authentication.service;

import com.hack.stock2u.constant.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public class AppGrantedAuthority implements GrantedAuthority {
  private final UserRole userRole;

  @Override
  public String getAuthority() {
    return userRole.getName();
  }
}
