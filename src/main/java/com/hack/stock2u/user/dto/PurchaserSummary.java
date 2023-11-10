package com.hack.stock2u.user.dto;

import lombok.Builder;

@Builder
public record PurchaserSummary(
    Long id,
    String name,
    String phone,
    String profileImageUrl,
    int buyCount,
    int reviewCount
) {}
