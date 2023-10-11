package com.hack.stock2u.authentication.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AuthRequestDto {

  public record Token(@NotNull @NotBlank String token) {}

}
