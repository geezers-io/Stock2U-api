package com.hack.stock2u.authentication.dto;

import com.hack.stock2u.constant.AuthVendor;
import com.hack.stock2u.constant.UserRole;
import com.hack.stock2u.models.SellerDetails;
import com.hack.stock2u.models.User;
import lombok.Builder;

@Builder
public record UserDetails(
    Long id,
    String name,
    String email,
    String phone,
    UserRole role,
    AuthVendor vendor,
    SellerDetails sellerDetails
) {
  public static UserDetails user(User user) {
    return UserDetails.builder()
        .id(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        .phone(user.getPhone())
        .role(user.getRole())
        .vendor(user.getVendor())
        .sellerDetails(user.getSellerDetails())
        .build();
  }
}
