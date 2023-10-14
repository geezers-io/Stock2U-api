package com.hack.stock2u.authentication.dto;

import com.hack.stock2u.constant.AuthVendor;
import com.hack.stock2u.constant.UserRole;
import com.hack.stock2u.models.SellerDetails;
import com.hack.stock2u.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record UserDetails(
    Long id,
    String name,
    String email,
    String phone,
    UserRole role,
    AuthVendor vendor,
    @Schema(nullable = true, description = "판매자 정보(사용자가 판매자라면 전달 됨)")
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
