package com.hack.stock2u.authentication.dto.doro;

public record DoroAddress(
    String roadAddr,
    String roadAddrPart1,
    String roadAddrPart2,
    String jibunAddr,
    String zipNo,
    String bdMgtSn,
    String detBdNmList,
    String bdNm
) {}
