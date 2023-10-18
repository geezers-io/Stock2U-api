package com.hack.stock2u.user.dto;

import com.hack.stock2u.models.User;
import lombok.Builder;

@Builder
public record SellerDetails(
    Long id,
    String username,
    String phone,
    int salesCount,
    int reviewCount
) {
  public static SellerDetails create(User u, int salesCount, int reviewCount) {
    return SellerDetails.builder()
        .id(u.getId())
        .username(u.getName())
        .phone(u.getPhone())
        .salesCount(salesCount)
        .reviewCount(reviewCount)
        .build();
  }
}
