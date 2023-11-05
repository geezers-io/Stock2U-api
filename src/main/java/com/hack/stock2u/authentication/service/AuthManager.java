package com.hack.stock2u.authentication.service;

import com.hack.stock2u.authentication.dto.SessionUser;
import com.hack.stock2u.models.User;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
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
  private final RedisTemplate<String, SessionUser> redisTemplate;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    Object phone = authentication.getPrincipal();
    Object id = authentication.getCredentials();
    ValueOperations<String, SessionUser> ops = redisTemplate.opsForValue();

    UserDetails userDetails = (UserDetails) userDetailsService.loadUserByUsername((String) phone);
    SessionUser user = SessionUser.user(userDetails.getUser());

    String key = sessionManager.getKey(phone, id);
    ops.set(key, user);

    redisTemplate.expire(key, 1, TimeUnit.HOURS);

    return new UsernamePasswordAuthenticationToken(phone, id, userDetails.getAuthorities());
  }

}
