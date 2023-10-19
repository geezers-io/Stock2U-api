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

public class ReservationResponseDto {
  @Builder
  public record GetReservation(
      Long id,
      String chatId,
      String title,
      //최근 메시지 미리보기
      String message,
      //만든시점부터 현재시간 빼야함 메세지 생성시간
      Date createdAt,
      String status,
      List<SimpleFile> productImages
  ) {
    public static GetReservation reserv(Reservation reservation,
                                        Product product, List<Attach> attaches) {
      List<SimpleFile> simpleFiles = attaches.stream().map(SimpleFile::attach).toList();
      return GetReservation.builder()
          .id(reservation.getId())
          .chatId(reservation.getChatId())
          .title(product.getTitle())
          .status(product.getStatus().getName())
          .productImages(simpleFiles)
          .build();

    }
  }
}

