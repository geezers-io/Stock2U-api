package com.hack.stock2u.user.dto;

import com.hack.stock2u.product.dto.SimpleProduct;
import java.util.List;

public record SellerSubscribeDetails(
    SellerDetails sellerDetails,
    List<SimpleProduct> currentProducts
) {}
