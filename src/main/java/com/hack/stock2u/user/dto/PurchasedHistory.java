package com.hack.stock2u.user.dto;

import java.util.Date;
import lombok.Builder;

@Builder
public record PurchasedHistory(
    Long productId,
    String thumbnailUrl,
    String title,
    String name,
    Date createdAt
) {}
