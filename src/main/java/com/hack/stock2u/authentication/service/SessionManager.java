package com.hack.stock2u.authentication.service;

import com.hack.stock2u.authentication.AuthException;
import com.hack.stock2u.models.User;
import com.hack.stock2u.user.UserException;
import com.hack.stock2u.user.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SessionManager {
  private final JpaUserRepository userRepository;
  private final RedisTemplate<String, User> redisTemplate;

  public User getSessionUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    validate(authentication);
    ValueOperations<String, User> ops = redisTemplate.opsForValue();

    String key = getKey(authentication.getPrincipal(), authentication.getCredentials());
    return ops.get(key);
  }

  public User getSessionUserByRdb() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    validate(authentication);

    log.warn("credentials: {}", authentication.getCredentials());
    log.warn("principal: {}", authentication.getPrincipal());
    return userRepository.findById((Long) authentication.getCredentials())
        .orElseThrow(UserException.NOT_FOUND_USER::create);
  }

  private void validate(Authentication authentication) {
    if (
        !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")
    ) {
      throw AuthException.IS_ANONYMOUS_USER.create();
    }
  }

  public String getKey(Object principal, Object credentials) {
    String phone = (String) principal;
    Long id = (Long) credentials;
    return "session:" + phone + ":" + id;
  }

}