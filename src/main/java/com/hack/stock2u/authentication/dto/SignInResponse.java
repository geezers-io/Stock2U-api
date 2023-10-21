package com.hack.stock2u.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record SignInResponse(boolean exists,
                             @Schema(nullable = true, description = "회원가입 페이지에서 사용할 email")
                            String email,
                             @Schema(
                                 nullable = true,
                                 description = "회원가입 인증 값(회원가입에 필요) * 무분별한 회원가입 방지"
                             )
                            String verification,
                             @Schema(nullable = true, description = "사용자 정보 객체")
                            UserDetails user
) {}
