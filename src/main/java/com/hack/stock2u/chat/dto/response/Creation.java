package com.hack.stock2u.chat.dto.response;

import com.hack.stock2u.constant.ChatMessageType;
import com.hack.stock2u.constant.ReservationStatusForChatList;
import lombok.Builder;

@Builder
public record Creation(
    ReservationStatusForChatList status,
    ChatMessageType type,
    long count,
    SimpleReservation simpleReservation,
    ChatMessageResponse chatMessageResponse
) {


}
