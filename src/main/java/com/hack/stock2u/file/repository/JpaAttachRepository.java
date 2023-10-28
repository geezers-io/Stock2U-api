package com.hack.stock2u.file.repository;

import com.hack.stock2u.models.Attach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaAttachRepository extends JpaRepository<Attach, Long> {
  @Query("select a from attachments a where a.product.id = :productId order by a.id limit 1")
  Attach getThumbnail(Long productId);
}
