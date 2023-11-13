package com.hack.stock2u.authentication.service;

import com.hack.stock2u.authentication.AuthException;
import com.hack.stock2u.authentication.dto.SessionUser;
import com.hack.stock2u.global.exception.GlobalException;
import com.hack.stock2u.models.User;
import com.hack.stock2u.user.UserException;
import com.hack.stock2u.user.repository.JpaUserRepository;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
  private final RedisTemplate<String, SessionUser> redisTemplate;

  public SessionUser getSessionUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    validate(authentication);

    String key = getKey(authentication.getPrincipal(), authentication.getCredentials());
    return getSessionUserKey(key);
  }

  public SessionUser getSessionUser(String key) {
    return getSessionUserKey(key);
  }
  
  public User getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(GlobalException.NOT_FOUND::create);
  }

  public User getSessionUserByRdb() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    validate(authentication);

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
    return "stock2u:session:" + principal + ":" + credentials;
  }

  private SessionUser getSessionUserKey(String key) {
    ValueOperations<String, SessionUser> ops = redisTemplate.opsForValue();
    SessionUser sessionUser = ops.get(key);
    if (sessionUser == null) {
      throw AuthException.EXPIRED_ACCOUNT.create();
    }

    return sessionUser;
  }

}
