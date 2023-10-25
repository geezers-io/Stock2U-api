package com.hack.stock2u.chat.dto.request;

public record ChatMessageRequest(
    Long roomId,
    Long userId
) {

}
