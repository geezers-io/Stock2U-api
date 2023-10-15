package com.hack.stock2u.chat.dto.response;

import java.util.Date;

public record ChatResponse(
    Long roomId,
    String message,
    Date timeStamp
) {
}
