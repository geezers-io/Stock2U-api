package com.hack.stock2u.chat.dto;

import com.hack.stock2u.constant.ChatMessageType;
import com.hack.stock2u.constant.ReservationStatus;
import java.util.Date;
import java.util.List;
import lombok.Builder;

@Builder
public record ChatMessageObjectForSerialize(
    ChatMessageType type,
    String username,
    String message,
    Date createdAt,
    String profileImageUrl,
    List<Long> imageUrl
) {

}
