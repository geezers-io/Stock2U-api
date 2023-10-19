package com.hack.stock2u.product.dto;

import com.hack.stock2u.file.dto.SimpleFile;

public record SimpleProduct(
    Long id,
    String title,
    SimpleFile bannerImage
) {}
