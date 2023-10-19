package com.hack.stock2u.user.repository;

import com.hack.stock2u.models.Subscription;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaSubscriptionRepository extends JpaRepository<Subscription, Long> {

  /**
   * 구매자와 판매자 아이디로 존재하는 대상의 ID 를 반환합니다.
   * @param uid 구매자 ID
   * @param sellerId 판매자 ID
   */
  @Query("""
    select s.id from subscriptions s where s.subscriber.id = :uid and s.target.id = :sellerId"""
  )
  Optional<Long> findByUserIds(Long uid, Long sellerId);

  Page<Subscription> findBySubscriberId(Long id, Pageable pageable);
}
