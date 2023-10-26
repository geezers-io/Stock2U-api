package com.hack.stock2u.chat.dto.response;

import com.hack.stock2u.models.ChatMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ChatMessageResponse(

    @Schema(required = true, description = "chatmessage에 id")
    @NotNull
    Long roomId,

    Long userId,
    String username,
    String message,
    Date createdAt

//    String image
) {

}
