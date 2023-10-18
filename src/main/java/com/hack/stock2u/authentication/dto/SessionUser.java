package com.hack.stock2u.authentication.dto;

import com.hack.stock2u.constant.AuthVendor;
import com.hack.stock2u.constant.UserRole;
import com.hack.stock2u.models.User;
import lombok.Builder;

@Builder
public record SessionUser(
    Long id,
    String username,
    String phone,
    String email,
    UserRole userRole
) {
  public static SessionUser user(User u) {
    return SessionUser.builder()
        .id(u.getId())
        .username(u.getName())
        .phone(u.getPhone())
        .email(u.getEmail())
        .userRole(u.getRole())
        .build();
  }
}
