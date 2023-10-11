package com.hack.stock2u.authentication.service;

import com.hack.stock2u.models.User;
import com.hack.stock2u.user.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService
    implements org.springframework.security.core.userdetails.UserDetailsService {
  private final JpaUserRepository userRepository;

  @Override
  public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(
      String username
  ) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException(""));
    return new UserDetails(user);
  }

}
