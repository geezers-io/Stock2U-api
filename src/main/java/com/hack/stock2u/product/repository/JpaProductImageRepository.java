package com.hack.stock2u.product.repository;

import com.hack.stock2u.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductImageRepository extends JpaRepository<ProductImage, Long> {}
