package com.hack.stock2u.chat.dto.response;

import lombok.Builder;

@Builder
public record AlertIdAndMessage(
    String userName,
    Long userId,
    Long reservationId,
    String message
) {
}
