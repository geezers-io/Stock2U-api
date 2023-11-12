package com.hack.stock2u.chat.repository;

import com.hack.stock2u.models.Report;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaReportRepository extends JpaRepository<Report, Long> {
  Optional<Report> findByTargetIdAndReporterId(Long targetId, Long reportedId);
}
