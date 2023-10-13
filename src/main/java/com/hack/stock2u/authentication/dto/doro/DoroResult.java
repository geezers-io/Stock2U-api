package com.hack.stock2u.authentication.dto.doro;

import java.util.List;

public record DoroResult(
    DoroCommon common,
    List<DoroAddress> juso
) {}
