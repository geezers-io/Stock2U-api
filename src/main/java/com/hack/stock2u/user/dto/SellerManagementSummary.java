package com.hack.stock2u.user.dto;

import com.hack.stock2u.chat.dto.response.SimpleReservation;
import com.hack.stock2u.constant.ReservationStatus;


public record SellerManagementSummary(
    Long reservationId,
    ReservationStatus status,
    String purchaserName

) {
}
