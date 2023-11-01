package com.hack.stock2u.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;

public record ReportRequest(
    @Schema(required = true, description = "신고 사유")
    @NotNull
    String reason,
    @Schema(required = true, description = "채팅방 id, 예약 id")
    @NotNull
    Long roomId

) {
}
