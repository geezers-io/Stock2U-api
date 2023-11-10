package com.hack.stock2u.chat.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;

public record PurchaserSellerReservationsResponse(
    ChatMessageResponse latestChat,
    SimpleReservation reservationSummary,
    @Schema(description = "안 읽은 채팅 수")
    long count
) {

}
