package com.hack.stock2u.product.dto;

import com.hack.stock2u.constant.ProductType;
import java.util.Date;

public record ProductSummary(
    Long id, String title, int price, ProductType type, Date expiredAt, Double distance,
    Double latitude, Double longitude, String thumbnailUrl
) {}
