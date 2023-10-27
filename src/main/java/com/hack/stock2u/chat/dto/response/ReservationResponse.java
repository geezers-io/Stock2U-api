package com.hack.stock2u.chat.dto.response;

import com.hack.stock2u.file.dto.SimpleFile;
import lombok.Builder;

//채팅방 조회에 사용될 예정
public class ReservationResponse {
  @Builder
  public record GetReservation(
      Long id,
      String name,
      ChatMessageResponse message,
      String status,
      SimpleFile productImage
  ) {

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

