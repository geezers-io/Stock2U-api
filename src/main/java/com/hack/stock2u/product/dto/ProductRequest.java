package com.hack.stock2u.product.dto;

import com.hack.stock2u.constant.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProductRequest {
  public record Create(
      @Schema(description = "게시글에 업로드할 이미지 아이디 리스트", required = true)
      @NotNull
      List<Long> fileIds,
      @Schema(description = "판매글 제목", required = true)
      @Size(min = 5, max = 50) @NotNull
      String title,
      @Schema(description = "재고 이름", required = true)
      @Size(min = 5, max = 50) @NotNull
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
      @Schema(description = "예약 한 건만 받기", required = true)
      @NotNull
      boolean onlyOneReserve,
      @Schema(description = "구매자에게 계좌 정보 보이기", required = true)
      @NotNull
      boolean showAccountDetails,
      @Schema(description = "게시 마감 기한", required = true)
      @NotNull
      Date expiredAt
  ) {}
}
