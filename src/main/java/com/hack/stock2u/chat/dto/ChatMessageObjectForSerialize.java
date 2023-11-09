package com.hack.stock2u.chat.dto;

import com.hack.stock2u.constant.ReservationStatus;
import java.util.Date;
import lombok.Builder;

@Builder
public record ChatMessageObjectForSerialize(
    String username,
    String message,
    Date createdAt,
    Long imageId
) {

}
