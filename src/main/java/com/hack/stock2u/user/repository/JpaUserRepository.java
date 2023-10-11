package com.hack.stock2u.user.repository;

import com.hack.stock2u.models.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends CrudRepository<User, Long> {
  Optional<User> findByEmail(String email);
}
