package com.hack.stock2u.chat.dto.response;

import com.hack.stock2u.constant.ReservationStatus;
import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.Reservation;
import com.hack.stock2u.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;

@Builder
public record ReservationMessageResponse(
    Long roomId,
    User sellerId,
    User purchaserId,
    String message,
    Date createdAt,
    @Schema(description = "예약 상태(예약 한 건만 받기 체크 && 예약 진행 중일 시 표기 됨)", nullable = true)
    ReservationStatus status
) {
  public static ReservationMessageResponse makingReservationMessage(Reservation reservation, Product product) {
    return ReservationMessageResponse.builder()
        .roomId(reservation.getId())
        .message(product.getName())
        .sellerId(reservation.getSeller())
        .purchaserId(reservation.getPurchaser())
        .createdAt(new Date())
        .status(product.getStatus())
        .build();

  }
}
