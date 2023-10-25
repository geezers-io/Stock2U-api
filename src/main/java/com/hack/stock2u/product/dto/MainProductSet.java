package com.hack.stock2u.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record MainProductSet(
    @Schema(description = "AI 추천 재고 리스트", required = true)
    List<ProductSummaryProjection> aiRecommends,
    @Schema(description = "마감 임박순 재고 리스트", required = true)
    List<ProductSummaryProjection> deadlines,
    @Schema(description = "내 인근 위치순 재고 리스트", required = true)
    List<ProductSummaryProjection> myNeighborhoods
) {}
