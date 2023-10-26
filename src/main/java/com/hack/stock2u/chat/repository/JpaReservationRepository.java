package com.hack.stock2u.chat.repository;

import com.hack.stock2u.models.Reservation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaReservationRepository extends JpaRepository<Reservation, Long> {
  List<Reservation> findByNameContaining(String productName);

}
