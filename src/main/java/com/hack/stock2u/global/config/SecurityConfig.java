package com.hack.stock2u.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
public class SecurityConfig {
  @Bean
  public void securityFilterChain(HttpSecurity http) throws Exception {
    http.formLogin().disable();
  }
}
