package com.hack.stock2u.chat.dto.response;

import java.util.Date;

public record ChatMessageResponse(

    String name,
    Long roomId,
    String message,
    Date createdAt
) {
}
