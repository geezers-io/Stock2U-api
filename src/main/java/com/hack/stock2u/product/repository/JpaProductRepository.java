package com.hack.stock2u.product.repository;

import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProductRepository extends JpaRepository<Product, Long> {
  List<Product> findTop5BySeller(User user);
}
