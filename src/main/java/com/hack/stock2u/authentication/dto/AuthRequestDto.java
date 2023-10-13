package com.hack.stock2u.authentication.dto;

import com.hack.stock2u.constant.AuthVendor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class AuthRequestDto {

  public record Token(String token) {}

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

      @NotNull AuthVendor vendor
  ) {}

}
