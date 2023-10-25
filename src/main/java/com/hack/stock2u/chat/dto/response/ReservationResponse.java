package com.hack.stock2u.chat.dto.response;

import com.hack.stock2u.file.dto.SimpleFile;
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

  }
}

