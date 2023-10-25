package com.hack.stock2u.chat.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//채팅 보내는 dto
public record SendChatMessage(
    Long roomId,
    Long userId,

    String sender,
    String message
) {
}
