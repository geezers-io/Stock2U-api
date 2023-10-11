package com.hack.stock2u.global.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record BasicErrorResponse(
    String errorCode,
    String message,
    @JsonIgnore HttpStatus httpStatus
) {}
