package com.hack.stock2u.chat.dto;

import com.hack.stock2u.constant.ChatAlertType;
import com.hack.stock2u.constant.ChatMessageType;
import lombok.Builder;

@Builder
public record MessageAlert(
    String message,
    String userName,
    ChatMessageType type,
    ChatAlertType status
) {
}
