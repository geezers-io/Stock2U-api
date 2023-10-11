package com.hack.stock2u.authentication.service.client;

import javax.validation.constraints.NotNull;

public record AuthUserDetail(@NotNull Long id, String email) {}
