package com.hack.stock2u.learn;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.AntPathMatcher;

public class AntPatternMatchTest {

  @Test
  @DisplayName("AntPattern 케이스들이 일치한다.")
  void ant() {
    assertThat(match("/api-docs/**", "/api-docs/swagger-config"))
        .isTrue();
    assertThat(match("/auth/signin", "/auth/signin")).isTrue();
    assertThat(match("", "/swagger-ui/index.html")).isTrue();
  }

  private boolean match(String pattern, String url) {
    return new AntPathMatcher().match(pattern, url);
  }

}
