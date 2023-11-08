package com.hack.stock2u.order.repository;

import com.hack.stock2u.models.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Buyer,Integer> {
  Buyer findByUserId(int userId);
}
