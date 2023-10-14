package com.hack.stock2u.authentication.dto;

import com.hack.stock2u.constant.AuthVendor;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class AuthRequestDto {

  public record SignInRequest(String authCode) {}

  public record AuthCode(
      @NotNull @NotBlank
      String authCode,
      @NotNull @NotBlank
      String phone
  ) {}

  public record SignupUserRequest(
      @NotNull @NotBlank
      String username,
      String email,
      @NotNull @NotBlank
      String phone,
      @NotNull @NotBlank
      String verification,

      @Schema
      @NotNull AuthVendor vendor
  ) {}

}
