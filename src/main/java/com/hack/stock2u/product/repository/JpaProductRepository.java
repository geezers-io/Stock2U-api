package com.hack.stock2u.product.repository;

import com.hack.stock2u.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProductRepository extends JpaRepository<Product, Long> {}
