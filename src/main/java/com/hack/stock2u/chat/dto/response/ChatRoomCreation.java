package com.hack.stock2u.chat.dto.response;

import com.hack.stock2u.constant.ChatAlertType;
import com.hack.stock2u.constant.ChatMessageType;
import lombok.Builder;

@Builder
public record ChatRoomCreation(
    ChatAlertType type,
    ChatMessageType chatMessageType,
    ChatRoomSummary chatRoomSummary
) {}
