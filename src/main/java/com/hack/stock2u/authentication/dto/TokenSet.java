package com.hack.stock2u.authentication.dto;

import javax.validation.constraints.NotNull;

public record TokenSet(@NotNull String accessToken, @NotNull String refreshToken) {}
