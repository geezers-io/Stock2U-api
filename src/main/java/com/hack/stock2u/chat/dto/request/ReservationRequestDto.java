package com.hack.stock2u.chat.dto.request;

import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;

public class ReservationRequestDto {

  public record CreateReservationRequest(
      @Schema(required = true, description = "제품 정보")
      @NotNull
      Long productId
  ){}

  public record FindMyReservationRequest(
      @Schema(required = true, description = "판매자")
      @NotNull
      User seller,

      @Schema(required = true, description = "구매자")
      @NotNull
      User customer,

      @Schema(required = true, description = "제품 정보")
      @NotNull
      Product product
  ){}

}
