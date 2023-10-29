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

  Optional<Reservation> findBySellerId(Long sellerId);

  Page<Reservation> findByPurchaserId(Long purchaserId, Pageable pageable);

  Optional<Reservation> findByPurchaserId(Long purchaser);
}
