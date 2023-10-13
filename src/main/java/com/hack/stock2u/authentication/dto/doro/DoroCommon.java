package com.hack.stock2u.authentication.dto.doro;

public record DoroCommon(
    String totalCount,
    int currentPage,
    int countPerPage,
    String errorCode,
    String errorMessage
) {}
