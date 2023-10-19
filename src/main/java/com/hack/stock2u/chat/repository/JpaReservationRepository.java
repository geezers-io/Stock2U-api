package com.hack.stock2u.chat.repository;

import com.hack.stock2u.models.Reservation;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaReservationRepository extends JpaRepository<Reservation, Long> {
  /**
   * 구매자와 연관된 예약리스트를 불러옵니다.
   */
  List<Reservation> findByPurchaserId(Long id);

  /**
   * 판매자와 연관된 예약리스트 불러옵니다.
   */
  List<Reservation> findBySellerId(Long id);

}
