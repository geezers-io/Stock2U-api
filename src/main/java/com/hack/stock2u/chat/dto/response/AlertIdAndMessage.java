package com.hack.stock2u.chat.dto.response;

import com.hack.stock2u.constant.ChatMessageType;
import com.hack.stock2u.constant.ReservationStatusForChatList;
import lombok.Builder;

@Builder
public record AlertIdAndMessage(
    ReservationStatusForChatList type,
    String userName,
    Long userId,
    Long reservationId,
    String message,
    Long imageId,
    ChatMessageType chatMessageType

) {
}
