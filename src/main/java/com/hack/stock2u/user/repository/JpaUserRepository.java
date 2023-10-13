package com.hack.stock2u.user.repository;

import com.hack.stock2u.constant.AuthVendor;
import com.hack.stock2u.models.User;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends CrudRepository<User, Long> {
  Optional<User> findByEmail(String email);

  @Query("select u from users u where u.email = :email and u.vendor = :vendor")
  Optional<User> findByEmailAndVendor(String email, AuthVendor vendor);

  Optional<User> findById(Long userId);

  Optional<User> findByPhone(String phone);
}
