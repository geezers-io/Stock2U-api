package com.hack.stock2u.chat.dto.response;

import com.hack.stock2u.file.dto.SimpleFile;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

public class ReservationResponse {
  @Builder
  public record GetReservation(
      Long id,
      String name,
      ChatMessageResponse message,
      String status,
      SimpleFile productImage
  ) {
    @QueryProjection
    public GetReservation(Long id, String name, ChatMessageResponse message,
                          String status, SimpleFile productImage) {
      this.id = id;
      this.name = name;
      this.message = message;
      this.status = status;
      this.productImage = productImage;
    }
  }
}

