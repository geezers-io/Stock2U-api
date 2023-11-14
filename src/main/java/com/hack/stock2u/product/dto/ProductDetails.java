package com.hack.stock2u.product.dto;

import com.hack.stock2u.constant.ProductType;
import com.hack.stock2u.constant.ReservationStatus;
import com.hack.stock2u.file.dto.SimpleFile;
import com.hack.stock2u.models.Attach;
import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.Reservation;
import com.hack.stock2u.user.dto.SellerDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.Builder;

@Builder
public record ProductDetails(
    Long id,
    String title,
    int price,
    ProductType type,
    @Schema(description = "재고 이름")
    String name,
    String description,
    int productCount,
    Long reservationId,
    Date expiredAt,
    @Schema(description = "예약 상태(예약 한 건만 받기 체크 && 예약 진행 중일 시 표기 됨)", nullable = true)
    ReservationStatus status,
    SellerDetails seller,

    @Schema(description = "요청한 사용자(구매자)가 구독한 판매자인 지 여부 (true 면 구독)")
    boolean isSubscribe,
    List<SimpleFile> productImages
) {
  public static ProductDetails create(
      Product p,
      SellerDetails sellerDetails,
      List<Attach> attaches,
      boolean isSubscribe,
      Optional<Reservation> reservation
  ) {
    List<SimpleFile> simpleFiles = attaches.stream().map(SimpleFile::attach).toList();
    return ProductDetails.builder()
        .id(p.getId())
        .title(p.getTitle())
        .name(p.getName())
        .price(p.getPrice())
        .type(p.getType())
        .status(reservation.map(Reservation::getStatus).orElse(null))
        .description(p.getDescription())
        .reservationId(reservation.map(Reservation::getId).orElse(null))
        .expiredAt(p.getExpiredAt())
        .productCount(p.getProductCount())
        .seller(sellerDetails)
        .productImages(simpleFiles)
        .isSubscribe(isSubscribe)
        .build();
  }
}
