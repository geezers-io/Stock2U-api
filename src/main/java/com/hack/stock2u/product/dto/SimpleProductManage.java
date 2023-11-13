package com.hack.stock2u.product.dto;

import com.hack.stock2u.constant.ProductType;
import com.hack.stock2u.models.Reservation;
import java.util.Date;
import lombok.Builder;

@Builder
public record SimpleProductManage(
    Long id,
    String title,
    ProductType type,
    Date creatAt
) {
  public static SimpleProductManage create(Reservation reservation) {
    return SimpleProductManage.builder()
        .id(reservation.getProduct().getId())
        .title(reservation.getProduct().getTitle())
        .type(reservation.getProduct().getType())
        .creatAt(reservation.getProduct().getCreateAt())
        .build();

  }
}
