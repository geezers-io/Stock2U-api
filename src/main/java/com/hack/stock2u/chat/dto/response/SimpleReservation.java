package com.hack.stock2u.chat.dto.response;

import com.hack.stock2u.constant.ReservationStatus;
import com.hack.stock2u.file.dto.SimpleFile;
import com.hack.stock2u.models.Attach;
import com.hack.stock2u.models.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
public record SimpleReservation(

    @Schema(description = "예약 id, 채팅방 id")
    Long id,
    @Schema(description = "게시글 제목")
    String title,
    @Schema(description = "상품 이름")
    String name,
    @Schema(description = "예약 상태")
    ReservationStatus status,
    @Schema(description = "썸네일 주소")
    SimpleThumbnailImage uploadUrl
) {
  public static SimpleReservation create(Reservation reservation,
                                         SimpleThumbnailImage simpleThumbnailImage) {
    return SimpleReservation.builder()
        .id(reservation.getId())
        .title(reservation.getProduct().getTitle())
        .name(reservation.getProduct().getName())
        .status(reservation.getStatus())
        .uploadUrl(simpleThumbnailImage)
        .build();
  }
}
