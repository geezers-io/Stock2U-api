package com.hack.stock2u.product.dto;

import com.hack.stock2u.constant.ProductType;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

public record ProductSummary(
    Long id, String title, int price, ProductType type, Date expiredAt, Double distance,
    Double latitude, Double longitude, String thumbnailUrl
) {}
