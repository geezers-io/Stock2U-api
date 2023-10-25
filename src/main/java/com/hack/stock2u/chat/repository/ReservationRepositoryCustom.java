package com.hack.stock2u.chat.repository;

import com.hack.stock2u.chat.dto.response.ReservationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationRepositoryCustom {
  Page<ReservationResponse> getReservations(Pageable pageable);
}
