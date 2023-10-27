package com.hack.stock2u.product.dto;

import com.hack.stock2u.constant.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProductRequest {
  public record Create(
      @Schema(description = "게시글에 업로드할 이미지 아이디 리스트", required = true)
      @NotNull
      List<Long> imageIds,
      @Schema(description = "판매글 제목", required = true)
      @Size(min = 4, max = 50, message = "제목은 최소 4글자 이상입니다.")
      String title,
      @Schema(description = "재고 이름", required = true)
      @Size(min = 2, max = 50, message = "재고 이름은 최소 2글자 이상입니다.")
      String name,
      @Schema(description = "재고 가격", enumAsRef = true)
      @NotNull
      Integer price,
      @Schema(description = "재고 분류", required = true)
      @NotNull
      ProductType type,
      @Schema(description = "재고 설명", required = true)
      @Size(max = 1000, message = "상세 설명은 1000글자를 넘으면 안됩니다.")
      String description,
      @Schema(description = "재고 수량", required = true)
      @NotNull
      Integer productCount,
      @Schema(description = "게시 마감 기한", required = true)
      @NotNull
      Date expiredAt
  ) {}
}
