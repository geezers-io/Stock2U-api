package com.hack.stock2u.user.repository;

import com.hack.stock2u.models.SellerDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSellerRepository extends JpaRepository<SellerDetails, Long> {}
