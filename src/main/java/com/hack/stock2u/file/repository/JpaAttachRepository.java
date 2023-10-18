package com.hack.stock2u.file.repository;

import com.hack.stock2u.models.Attach;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAttachRepository extends JpaRepository<Attach, Long> {}
