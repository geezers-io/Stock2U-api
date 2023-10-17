package com.hack.stock2u.chat.dto.request;

import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ChatRoomRequestDto {
  public record CreateReservationRequest(
      @Schema(required = true, description = "제품 정보")
      @NotNull
      Long productId,
      @Schema(required = true, description = "구매자")
      @NotNull
      Long purchaserId
  ){}

  public record FindMyReservationRequest(
      @Schema(required = true, description = "판매자")
      @NotNull @NotBlank
      User seller,

      @Schema(required = true, description = "구매자")
      @NotNull @NotBlank
      User customer,

      @Schema(required = true, description = "제품 정보")
      @NotNull @NotBlank
      Product product
  ){}

}
