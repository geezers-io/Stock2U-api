package com.hack.stock2u.chat.repository;

import com.hack.stock2u.models.Reservation;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaChatRoomRepository extends CrudRepository<Reservation, Long> {


  List<Reservation> findAllByName(String name, Sort sort);
}
