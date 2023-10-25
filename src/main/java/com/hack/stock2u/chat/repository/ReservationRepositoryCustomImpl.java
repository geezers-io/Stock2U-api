package com.hack.stock2u.chat.repository;

import com.hack.stock2u.chat.dto.response.ReservationResponse;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;

public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom{
//  private JPAQueryFactory queryFactory;
//
//  public ReservationRepositoryCustomImpl(EntityManager em){
//    this.queryFactory = new JPAQueryFactory(em);
//  }
  @Override
  public Page<ReservationResponse> getReservations(Pageable pageable) {
    return null;
  }
}
