package com.hack.stock2u.authentication.service;

import com.hack.stock2u.constant.AuthVendor;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  private final RedisTemplate<String, AuthVendor> redisTemplate;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    Object phone = authentication.getPrincipal();
    Object id = authentication.getCredentials();

    UserDetails userDetails = (UserDetails) userDetailsService.loadUserByUsername((String) phone);

    ValueOperations<String, AuthVendor> ops = redisTemplate.opsForValue();
    // FIX: 미완성 로직
    String key = "session:" + phone + ":" + id;
    ops.set(key, userDetails.getVendor());
    redisTemplate.expire(key, 1, TimeUnit.HOURS);

    return new UsernamePasswordAuthenticationToken(phone, id, userDetails.getAuthorities());
  }

}
