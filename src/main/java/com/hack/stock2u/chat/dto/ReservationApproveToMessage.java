package com.hack.stock2u.chat.dto;

import com.hack.stock2u.constant.ReservationStatus;
import com.hack.stock2u.models.Reservation;

public record ReservationApproveToMessage(
    ReservationStatus status,
    Reservation reservation
) {
}
