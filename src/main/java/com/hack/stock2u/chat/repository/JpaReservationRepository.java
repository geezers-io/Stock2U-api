package com.hack.stock2u.chat.repository;

import com.hack.stock2u.models.Reservation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaReservationRepository extends JpaRepository<Reservation, Long> {

  Page<Reservation> findBySellerIdOrderByBasicDateCreatedAtDesc(Long sellerId, Pageable pageable);

  Page<Reservation> findByPurchaserIdOrderByBasicDateCreatedAtDesc(
      Long purchaserId, Pageable pageable);

  @Query("select r from reservations r where r.purchaser.id = :pid and r.seller.id = :sid")
  Optional<Reservation> findByBothUserId(Long pid, Long sid);

}
