package com.hack.stock2u.authentication.dto;

import com.hack.stock2u.constant.AuthVendor;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AuthRequestDto {

  public record SignInRequest(
      @Schema(required = true, description = "OAuth 인증 코드")
      @NotBlank
      String authCode
  ) {}

  public record AuthCode(
      @Schema(required = true, description = "OAuth 인증 코드")
      @NotBlank
      String authCode,
      @Schema(required = true, description = "휴대폰 번호")
      @Pattern(regexp = "^[0-9]{11}", message = "휴대폰 번호는 010XXXXXXXX 형식이어야 합니다.")
      @Size(message = "휴대폰 번호는 010XXXXXXXX 형식이어야 합니다.", min = 11, max = 11)
      String phone
  ) {}

  public record SignupPurchaserRequest(
      @Schema(required = true, description = "회원이름")
      @NotNull @NotBlank
      @Pattern(regexp = "/^[a-zA-Z0-9ㄱ-ㅎ가-힣]{3,15}$/", message = "닉네임은 최소 3글자 이상 15글자 이하입니다.")
      String username,
      @Schema(required = true, description = "이메일", example = "email@naver.com")
      @Pattern(
          regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
          message = "이메일 형식이 아닙니다."
      )
      String email,
      @Schema(required = true, description = "휴대폰 번호")
      @NotNull @NotBlank
      @Pattern(regexp = "^[0-9]{11}", message = "휴대폰 번호는 010XXXXXXXX 형식이어야 합니다.")
      String phone,
      @Schema(required = true, description = "회원가입 인증코드")
      @NotNull @NotBlank
      String verification,

      @Schema(required = true, description = "외부 인증업체")
      @NotNull AuthVendor vendor
  ) {}

  public record SignupSellerRequest(
      @Schema(required = true, description = "회원이름")
      @NotNull @NotBlank
      @Pattern(regexp = "/^[a-zA-Z0-9ㄱ-ㅎ가-힣]{3,15}$/", message = "닉네임은 최소 3글자 이상 15글자 이하입니다.")
      String username,
      @Schema(required = true, description = "이메일", example = "email@naver.com")
      @Pattern(
          regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
          message = "이메일 형식이 아닙니다."
      )
      String email,

      @Schema(required = true, description = "사업자등록번호")
      @NotBlank
      String licenseNumber,
      @Schema(required = true, description = "업종")
      @NotBlank
      String industry,
      @Schema(required = true, description = "사업지명")
      @NotBlank
      String industryName,
      @Schema(required = true, description = "소재지(도로명 주소 API 로 기입바람)")
      @NotBlank
      String location,
      @Schema(required = true, description = "은행 이름(은행 리스트 조회 API 로 기입바람)")
      @NotBlank
      String bankName,
      @Schema(required = true, description = "계좌번호")
      @NotBlank
      String account,
      @Schema(required = true, description = "휴대폰 번호")
      @NotBlank
      @Pattern(regexp = "^[0-9]{11}", message = "휴대폰 번호는 010XXXXXXXX 형식이어야 합니다.")
      String phone,
      @Schema(required = true, description = "회원가입 인증코드")
      @NotBlank
      String verification,

      @Schema(required = true, description = "외부 인증업체")
      @NotNull AuthVendor vendor
  ) {}

}
