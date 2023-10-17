package com.hack.stock2u.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PurchaserRequest {
  public record Update(
      @Schema(required = true, description = "변경할 휴대폰 번호에 대한 인증코드")
      @Min(value = 6, message = "인증코드 형식이 유효하지 않습니다. (6자리)")
      String authCode,
      @Schema(required = true, description = "휴대폰 번호")
      @Pattern(regexp = "^[0-9]{11}", message = "휴대폰 번호는 010XXXXXXXX 형식이어야 합니다.")
      @Size(message = "휴대폰 번호는 010XXXXXXXX 형식이어야 합니다.", min = 11, max = 11)
      String phone
  ) {}
}
