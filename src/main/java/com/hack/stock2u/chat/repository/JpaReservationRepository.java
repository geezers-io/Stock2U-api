package com.hack.stock2u.chat.repository;

import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.Reservation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaReservationRepository extends JpaRepository<Reservation, Long> {

  Page<Reservation> findBySellerIdOrderByBasicDateCreatedAtDesc(Long sellerId, Pageable pageable);

  Page<Reservation> findByPurchaserIdOrderByBasicDateCreatedAtDesc(
      Long purchaserId, Pageable pageable);

  @SuppressWarnings("checkstyle:Indentation")
  @Query("""
    select r from reservations r
    where r.purchaser.id = :pid and r.seller.id = :sid and r.product.id = :productId
  """)
  Optional<Reservation> findByBothUserId(
      @Param("pid") Long pid, @Param("sid") Long sid, @Param("productId") Long productId
  );

  Optional<Reservation> findByProductAndPurchaserId(Product product, Long pid);

}
