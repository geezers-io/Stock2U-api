package com.hack.stock2u.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;

public class SellerRequest {
  public record LocationUpdate(
      @Schema(description = "소재지(도로명 주소 API 사용)", required = true)
      String location,

      @Schema(description = "위도", required = true)
      @NotNull
      Double latitude,

      @Schema(description = "경도", required = true)
      @NotNull
      Double longitude
  ) {}
}
