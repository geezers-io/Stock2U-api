package com.hack.stock2u.user.repository;

import com.hack.stock2u.models.QBuyer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PurchaserDslRepository {

  private final JPAQueryFactory queryFactory;


  public Long getBuyCount(Long purchaserId) {
    return queryFactory.select(QBuyer.buyer.count())
        .from(QBuyer.buyer)
        .where(QBuyer.buyer.purchaser.id.eq(purchaserId))
        .fetchFirst();
  }

}
