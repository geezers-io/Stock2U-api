package com.hack.stock2u.authentication.service;

import com.hack.stock2u.constant.AuthVendor;
import com.hack.stock2u.constant.UserRole;
import com.hack.stock2u.models.User;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {
  private final User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    UserRole role = user.getRole();
    AppGrantedAuthority authority = new AppGrantedAuthority(role);
    return Collections.singletonList(authority);
  }

  public String getEmail() {
    return user.getEmail();
  }

  public AuthVendor getVendor() { return user.getVendor(); }

  @Override
  public String getPassword() {
    return user.getEmail();
  }

  @Override
  public String getUsername() {
    return user.getName();
  }

  @Override
  public boolean isAccountNonExpired() {
    return user.getDisabledAt() == null;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    Date removedAt = user.getBasicDate().getRemovedAt();
    Date disabledAt = user.getDisabledAt();
    return removedAt == null && disabledAt == null;
  }
}
