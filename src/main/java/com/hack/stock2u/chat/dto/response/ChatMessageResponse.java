package com.hack.stock2u.chat.dto.response;

import com.hack.stock2u.models.ChatMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ChatMessageResponse(

    String username,
    String message,
    Date createdAt,
    boolean read

) {
  public static ChatMessageResponse create(
      ChatMessage chatMessage) {
    return ChatMessageResponse.builder()
        .username(chatMessage.getUsername())
        .message(chatMessage.getMessage())
        .createdAt(chatMessage.getCreatedAt())
        .read(chatMessage.isRead())
        .build();
  }
}
