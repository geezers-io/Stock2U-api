package com.hack.stock2u.chat.dto.request;

import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.Reservation;
import com.hack.stock2u.models.User;
import lombok.Builder;

@Builder
public record ReservationRequest(
    Long id,
    String chatId,
    Product productId,
    User seller,
    User customer

) {
  public static ReservationRequest reserv(Reservation reservation) {
    return ReservationRequest.builder()
        .id(reservation.getId())
        .chatId(reservation.getChatId())
        .productId(reservation.getProduct())
        .seller(reservation.getSeller())
        .customer(reservation.getCustomer())
        .build();
  }
}
