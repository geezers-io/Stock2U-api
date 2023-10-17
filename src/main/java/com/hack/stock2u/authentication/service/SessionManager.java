package com.hack.stock2u.authentication.service;

import com.hack.stock2u.authentication.AuthException;
import com.hack.stock2u.models.User;
import com.hack.stock2u.user.UserException;
import com.hack.stock2u.user.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SessionManager {
  private final JpaUserRepository userRepository;

  public User getSessionUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (
        !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")
    ) {
      throw AuthException.IS_ANONYMOUS_USER.create();
    }
    log.warn("credentials: {}", authentication.getCredentials());
    log.warn("principal: {}", authentication.getPrincipal());
    return userRepository.findById((Long) authentication.getCredentials())
        .orElseThrow(UserException.NOT_FOUND_USER::create);
  }

}
