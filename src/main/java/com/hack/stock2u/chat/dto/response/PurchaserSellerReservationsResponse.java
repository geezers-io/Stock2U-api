package com.hack.stock2u.chat.dto.response;


public record PurchaserSellerReservationsResponse(
    ChatMessageResponse latestChat,
    SimpleReservation reservationSummary
) {}
