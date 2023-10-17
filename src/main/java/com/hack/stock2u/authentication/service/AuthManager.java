package com.hack.stock2u.authentication.service;

import com.hack.stock2u.constant.AuthVendor;
import com.hack.stock2u.models.User;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthManager implements AuthenticationManager {
  private final UserDetailsService userDetailsService;
  private final SessionManager sessionManager;
  private final RedisTemplate<String, User> redisTemplate;

  @Value("${spring.session.timeout}")
  private Integer timeout;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    Object phone = authentication.getPrincipal();
    Object id = authentication.getCredentials();

    UserDetails userDetails = (UserDetails) userDetailsService.loadUserByUsername((String) phone);

    ValueOperations<String, User> ops = redisTemplate.opsForValue();

    String key = sessionManager.getKey(phone, id);
    ops.set(key, userDetails.getUser());
    redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);

    return new UsernamePasswordAuthenticationToken(phone, id, userDetails.getAuthorities());
  }

}
