package com.hack.stock2u.global.config;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

class SecurityConfigTest {
  SecurityConfig config = new SecurityConfig(null, null);
  Logger logger = LoggerFactory.getLogger("SecurityConfigTest");

  @Test
  @DisplayName("PasswordEncoder 를 가져온다.")
  void getPasswordEncoder() {
    PasswordEncoder passwordEncoder = config.passwordEncoder();
    assertThat(passwordEncoder).isNotNull();
  }

  @Test
  @DisplayName("비밀번호 해시연산을 성공적으로 수행한다.")
  void hashPassword() {
    String password = "@KDigitalhackerton123";
    PasswordEncoder passwordEncoder = config.passwordEncoder();
    String en = passwordEncoder.encode(password);
    logger.debug("encrypted password: {}", en);
    assertThat(en).startsWith("{bcrypt}");
  }
}
