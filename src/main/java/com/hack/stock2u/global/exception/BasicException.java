package com.hack.stock2u.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BasicException extends RuntimeException {
  private final BasicErrorResponse errorResponse;
}
