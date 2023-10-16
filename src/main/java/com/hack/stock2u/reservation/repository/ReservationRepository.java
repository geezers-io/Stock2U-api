package com.hack.stock2u.reservation.repository;

import com.hack.stock2u.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {}
