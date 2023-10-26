package com.hack.stock2u.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;

public record ReservationApproveRequest(

    @Schema(required = true, description = "상품 ID")
    @NotNull
    Long productId,
    @Schema(required = true, description = "구매자 ID")
    @NotNull
    Long purchaserId,

    @Schema(required = true, description = "판매자 ID")
    @NotNull
    Long sellerId,
    @Schema(required = true, description = "chatmessage에 room_id, reservation에 id")
    @NotNull
    Long roomId
) {
}
