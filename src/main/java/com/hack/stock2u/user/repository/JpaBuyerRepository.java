package com.hack.stock2u.user.repository;

import com.hack.stock2u.models.Buyer;
import com.hack.stock2u.models.User;
import java.time.LocalDate;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface JpaBuyerRepository extends JpaRepository<Buyer, Long> {
  //  @Query("select count() from buyers b where b.seller.id = :id")
  int countBySeller(User user);

}
