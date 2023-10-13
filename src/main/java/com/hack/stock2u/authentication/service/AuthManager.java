package com.hack.stock2u.authentication.service;

import com.hack.stock2u.models.User;
import com.hack.stock2u.user.UserException;
import com.hack.stock2u.user.repository.JpaUserRepository;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthManager implements AuthenticationManager {
  private final JpaUserRepository userRepository;
  private final RedisTemplate<String, Long> redisTemplate;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    Object phone = authentication.getPrincipal();
    Object id = authentication.getCredentials();

    Optional<User> userOptional = userRepository.findByPhone((String) phone);

    if (userOptional.isEmpty()) {
      throw UserException.NOT_FOUND_USER.create();
    }

    ValueOperations<String, Long> ops = redisTemplate.opsForValue();
    // FIX: 미완성 로직
    String key = "session:" + (String) phone;
    ops.set(key, (Long) id);
    redisTemplate.expire(key, 1, TimeUnit.HOURS);

    return new UsernamePasswordAuthenticationToken(phone, id);
  }

}
