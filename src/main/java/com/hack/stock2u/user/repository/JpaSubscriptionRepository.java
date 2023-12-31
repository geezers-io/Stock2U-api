package com.hack.stock2u.user.repository;

import com.hack.stock2u.models.Subscription;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaSubscriptionRepository extends JpaRepository<Subscription, Long> {

  /**
   * 구매자와 판매자 아이디로 존재하는 대상의 ID 를 반환합니다.

   * @param uid 구매자 Id
   * @param sellerId 판매자 ID
   */
  @Query("""
    select s.id from subscriptions s where s.subscriber.id = :uid and s.target.id = :sellerId"""
  )
  Optional<Long> findByUserIds(@Param("uid") Long uid, @Param("sellerId") Long sellerId);

  Page<Subscription> findBySubscriberId(Long id, Pageable pageable);

  Optional<Long> findBySubscriberId(Long purchaserId);

  @Query(
      "select sub from subscriptions sub where sub.subscriber.id = :pid and sub.target.id = :sid"
  )
  Optional<Long> existsBothUserId(@Param("pid") Long pid, @Param("sid") Long sid);
}
