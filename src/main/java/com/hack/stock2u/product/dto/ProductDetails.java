package com.hack.stock2u.product.dto;

import com.hack.stock2u.constant.ProductType;
import com.hack.stock2u.constant.ReservationStatus;
import com.hack.stock2u.file.dto.SimpleFile;
import com.hack.stock2u.models.Attach;
import com.hack.stock2u.models.Product;
import com.hack.stock2u.user.dto.SellerDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import java.util.List;
import lombok.Builder;

@Builder
public record ProductDetails(
    Long id,
    String title,
    int price,
    ProductType type,
    String description,
    boolean onlyOneReserve,
    boolean showAccountDetails,
    Date expiredAt,
    @Schema(description = "예약 상태(예약 한 건만 받기 체크 && 예약 진행 중일 시 표기 됨)", nullable = true)
    ReservationStatus status,
    Double latitude,
    Double longtitude,
    SellerDetails seller,
    List<SimpleFile> productImages
) {
  public static ProductDetails create(
      Product p, SellerDetails sellerDetails, List<Attach> attaches
  ) {
    List<SimpleFile> simpleFiles = attaches.stream().map(SimpleFile::attach).toList();
    return ProductDetails.builder()
        .id(p.getId())
        .title(p.getTitle())
        .price(p.getPrice())
        .type(p.getType())
        .description(p.getDescription())
        .onlyOneReserve(p.isOnlyOneReserve())
        .showAccountDetails(p.isShowAccountDetails())
        .expiredAt(p.getExpiredAt())
        .status(p.getStatus())
        .latitude(p.getLatitude())
        .longtitude(p.getLongtitude())
        .seller(sellerDetails)
        .productImages(simpleFiles)
        .build();
  }
}
