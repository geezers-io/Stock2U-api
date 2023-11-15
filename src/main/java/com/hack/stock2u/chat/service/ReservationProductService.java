package com.hack.stock2u.chat.service;

import com.hack.stock2u.chat.repository.ReservationProductDslRepository;
import com.hack.stock2u.product.dto.ProductSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReservationProductService {
  private final ReservationProductDslRepository reservationProductRepository;

  public ProductSummary getProductByReservationId(Long reservationId) {
    return reservationProductRepository.getProductSummaryByReservationId(reservationId);
  }

}
