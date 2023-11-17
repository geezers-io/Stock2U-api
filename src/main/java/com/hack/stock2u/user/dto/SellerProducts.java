package com.hack.stock2u.user.dto;

import com.hack.stock2u.constant.ProductType;
import com.hack.stock2u.models.Product;
import java.util.Date;
import lombok.Builder;

@Builder
public record SellerProducts(
    Long productId,
    String title,
    Date createAt,
    ProductType type,
    String thumbnailImage

) {
}
