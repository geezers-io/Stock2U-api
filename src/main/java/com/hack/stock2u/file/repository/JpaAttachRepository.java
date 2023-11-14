package com.hack.stock2u.file.repository;

import com.hack.stock2u.models.Attach;
import com.hack.stock2u.models.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaAttachRepository extends JpaRepository<Attach, Long> {
  Attach findFirstByProductIdOrderById(Long productId);

  List<Attach> findByProduct(Product product);

}
