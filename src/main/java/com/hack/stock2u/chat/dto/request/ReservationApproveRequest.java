package com.hack.stock2u.chat.dto.request;

public record ReservationApproveRequest(
    Long productId,
    Long purchaserId,
    Long sellerId,
    Long roomId
) {
}
