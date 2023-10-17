package com.hack.stock2u.user.repository;

import com.hack.stock2u.models.Buyer;
import com.hack.stock2u.models.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaBuyerRepository extends JpaRepository<Buyer, Long> {
  //  @Query("select count() from buyers b where b.seller.id = :id")
  int countBySeller(User user);
}
