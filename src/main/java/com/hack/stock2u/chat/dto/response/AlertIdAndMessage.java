package com.hack.stock2u.chat.dto.response;

import lombok.Builder;

@Builder
public record AlertIdAndMessage(
    Long reservationId,
    String message
) {
}
