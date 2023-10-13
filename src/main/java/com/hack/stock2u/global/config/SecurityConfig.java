package com.hack.stock2u.global.config;

import com.hack.stock2u.authentication.service.AuthManager;
import com.hack.stock2u.authentication.service.UserDetailService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.CorsRegistry;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
  private final UserDetailService userDetailService;
  private final AuthManager authManager;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    disableMvcSettings(http);

    http.cors().configurationSource(configurationSource());
    http.userDetailsService(userDetailService);
    http.authenticationManager(authManager);
    http.authorizeRequests()
        .antMatchers("/**").permitAll();

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource configurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("*"));
    configuration.setAllowedMethods(List.of("*"));
    UrlBasedCorsConfigurationSource source =
        new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  /**
   * 암호화를 수행하는 PasswordEncoder 객체를 반환합니다.
   */
  //  @Bean
  //  public PasswordEncoder passwordEncoder() {
  //    String idForEncode = "bcrypt";
  //    BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
  //    Map<String, PasswordEncoder> store = new HashMap<>();
  //    store.put(idForEncode, bcrypt);
  //    return new DelegatingPasswordEncoder(idForEncode, store);
  //  }
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  private void disableMvcSettings(HttpSecurity http) throws Exception {
    http.formLogin().disable();
    http.csrf().disable();
    http.logout().disable();
  }

}
