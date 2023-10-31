package com.hack.stock2u.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChangeStatusRequest(
    @Schema(description =
              "PROGRESS(\"예약 진행중\"),\n"
            + "CANCEL(\"예약 취소됨\"),\n"
            + "COMPLETION(\"구매 완료됨\")")
    String status,
    Long reservationId
) {
}
