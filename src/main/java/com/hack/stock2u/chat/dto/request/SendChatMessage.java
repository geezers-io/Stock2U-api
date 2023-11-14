package com.hack.stock2u.chat.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//채팅 보내는 dto
public record SendChatMessage(
    @Schema(required = true, description = "chatmessage에 id")
    @NotNull
    Long roomId,
    @Schema(required = true, description = "보내는 사용자 id")
    @NotNull
    Long userId,
    @Schema(required = true, description = "보내는 사용자 name")
    @NotNull
    String sender,
    @Schema(description = "메세지")
    String message,

    @Schema(description = "이미지")
    List<Long> imageIds
) {
}
