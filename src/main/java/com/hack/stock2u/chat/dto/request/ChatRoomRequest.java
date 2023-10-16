package com.hack.stock2u.chat.dto.request;

public record ChatRoomRequest(
    Long id,
    Long productId,
    Long sellerId,
    Long customerId
) {

}
