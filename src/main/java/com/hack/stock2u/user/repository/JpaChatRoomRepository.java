package com.hack.stock2u.user.repository;

import com.hack.stock2u.models.Reservation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaChatRoomRepository extends CrudRepository<Reservation, Long> { }
