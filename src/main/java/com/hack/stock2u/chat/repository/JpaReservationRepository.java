package com.hack.stock2u.chat.repository;

import com.hack.stock2u.models.Reservation;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaReservationRepository extends JpaRepository<Reservation, Long> {
}
