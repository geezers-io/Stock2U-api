package com.hack.stock2u.chat.dto.response;

import com.hack.stock2u.models.ChatMessage;
import java.util.Date;
import lombok.Builder;

@Builder
public record ChatMessageResponse(

    Long roomId,
    Long userId,
    String username,
    String message,
    Date createdAt

//    String image
) {

}
