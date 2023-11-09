package com.hack.stock2u.chat.dto.request;

import com.hack.stock2u.models.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;

public record ReservationApproveRequest(
    @Schema(required = true, description = "chatmessage에 room_id, reservation에 id")
    @NotNull
    Long roomId


) {
}
