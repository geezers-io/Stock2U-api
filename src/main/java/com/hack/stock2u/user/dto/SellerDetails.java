package com.hack.stock2u.user.dto;

import com.hack.stock2u.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record SellerDetails(
    Long id,
    String username,
    String phone,
    String profileImageUrl,
    @Schema(description = "판매 재고 갯수", required = true)
    int salesCount,
    @Schema(description = "받은 리뷰 갯수", required = true)
    int reviewCount
) {
  public static SellerDetails create(User u, String profileImageUrl, int salesCount, int reviewCount) {
    return SellerDetails.builder()
        .id(u.getId())
        .username(u.getName())
        .phone(u.getPhone())
        .profileImageUrl(profileImageUrl)
        .salesCount(salesCount)
        .reviewCount(reviewCount)
        .build();
  }
}
