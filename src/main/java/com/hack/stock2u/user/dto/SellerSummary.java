package com.hack.stock2u.user.dto;

import lombok.Builder;

@Builder
public record SellerSummary(
    Long id,
    String name,
    String phone,
    String avatarUrl,
    int salesCount,
    int reviewCount,
    String location,
    String bankName,
    String account
) {}
