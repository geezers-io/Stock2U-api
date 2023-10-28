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
    String username,
    String message,
    Date createdAt

//    String image
) {
  public static ChatMessageResponse create(
      ChatMessage chatMessage) {
    return ChatMessageResponse.builder()
        .roomId(chatMessage.getRoomId())
        .username(chatMessage.getUserName())
        .message(chatMessage.getMessage())
        .createdAt(chatMessage.getCreatedAt())
        .build();
  }
}
