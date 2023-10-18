package com.hack.stock2u.product.dto;

import com.hack.stock2u.models.Attach;
import com.hack.stock2u.models.ProductImage;
import java.util.List;

public record ProductImageSet(List<Attach> attaches, List<ProductImage> productImages) {}
