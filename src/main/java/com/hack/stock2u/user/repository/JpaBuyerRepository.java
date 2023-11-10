package com.hack.stock2u.user.repository;

import com.hack.stock2u.models.Buyer;
import com.hack.stock2u.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaBuyerRepository extends JpaRepository<Buyer, Long> {
  //  @Query("select count() from buyers b where b.seller.id = :id")
  int countBySeller(User user);

  Page<Buyer> findByPurchaserId(Long userId, Pageable pageable);
}
