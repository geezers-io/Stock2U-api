package com.hack.stock2u.chat.repository;

import com.hack.stock2u.models.Reservation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaReservationRepository extends JpaRepository<Reservation, Long> {

  Page<Reservation> findBySellerId(Long sellerId, Pageable pageable);

  Page<Reservation> findByPurchaserId(Long purchaserId, Pageable pageable);

  Page<Reservation> findByProductTitleContainingAndPurchaserId(
      String title, Long purchaserId, Pageable pageable);

  Page<Reservation> findByProductTitleContainingAndSellerId(
      String title, Long sellerId, Pageable pageable);
}
