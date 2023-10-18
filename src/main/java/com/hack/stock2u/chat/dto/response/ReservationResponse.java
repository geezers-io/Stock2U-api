package com.hack.stock2u.chat.dto.response;

import com.hack.stock2u.constant.ReservationStatus;
import com.hack.stock2u.file.dto.SimpleFile;
import com.hack.stock2u.models.Attach;
import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.Reservation;
import com.hack.stock2u.models.User;
import java.util.Date;
import java.util.List;
import lombok.Builder;

@Builder
public record ReservationResponse(
    Long id,
    String chatId,
    String title,
    int price,
    Date expiredAt,
    Long sellerId,
    Long customerId,
    String status,
    String uploadPath,
    List<SimpleFile> productImages
) {
  public static ReservationResponse reserv(Reservation reservation, List<Attach> attaches) {
    List<SimpleFile> simpleFiles = attaches.stream().map(SimpleFile::attach).toList();
    return ReservationResponse.builder()
        .id(reservation.getId())
        .chatId(reservation.getChatId())
        .title(reservation.getProduct().getTitle())
        .price(reservation.getProduct().getPrice())
        .expiredAt(reservation.getProduct().getExpiredAt())
        .sellerId(reservation.getSeller().getId())
        .customerId(reservation.getPurchaser().getId())
        .productImages(simpleFiles)
        .build();
  }
}
