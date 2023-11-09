package com.hack.stock2u.chat.dto.response;

import com.hack.stock2u.constant.ReservationStatus;
import com.hack.stock2u.models.ChatMessage;
import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.Reservation;
import com.hack.stock2u.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ReservationMessageResponse(

    @Schema(required = true, description = "구매자 이름")
    String purchaserName,
    @Schema(required = true, description = "자동 발송 메세지 내용")
    @NotNull
    String message,
    @Schema(required = true, description = "메세지 전송 시간")
    @NotNull
    Date createdAt,
    @Schema(description = "예약 상태(예약 한 건만 받기 체크 && 예약 진행 중일 시 표기 됨)", nullable = true)
    ReservationStatus status,
    @Schema(description = "프로필 사진")
    Long imageId
) {
  public static ReservationMessageResponse makingReservationMessage(
      Reservation reservation,
       User purchaser,
      ChatMessage chatMessage) {
    return ReservationMessageResponse.builder()
        .message(chatMessage.getMessage())
        .purchaserName(purchaser.getName())
        .createdAt(new Date())
        .status(reservation.getStatus())
        .build();
  }
}
