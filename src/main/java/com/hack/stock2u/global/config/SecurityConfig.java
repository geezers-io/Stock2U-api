package com.hack.stock2u.global.config;

import com.hack.stock2u.authentication.service.AuthAccessDeniedHandler;
import com.hack.stock2u.authentication.service.AuthManager;
import com.hack.stock2u.authentication.service.UserDetailService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
  private final UserDetailService userDetailService;
  private final AuthManager authManager;
  private final AuthAccessDeniedHandler accessDeniedHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    disableMvcSettings(http);

    http.userDetailsService(userDetailService);
    http.authenticationManager(authManager);
    http.authorizeRequests()
            .antMatchers(
                "/auth/signin", "/auth/signup/**", "/auth/signin-url",
                "/swagger-ui/*", "/docs", "/api-docs*", "/swagger-ui/**"
            ).permitAll()
            .antMatchers("/test/admin").hasRole("ADMIN")
            .anyRequest()
            .authenticated();

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource configurationSource() {
    CorsConfiguration config = new CorsConfiguration();
//    config.setAllowCredentials(true);
    config.setExposedHeaders(List.of("*"));
    config.setAllowedOriginPatterns(List.of("*"));
    config.setAllowedMethods(List.of("*"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  /**
   * 암호화를 수행하는 PasswordEncoder 객체를 반환합니다.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  private void disableMvcSettings(HttpSecurity http) throws Exception {
    http.formLogin().disable();
    http.csrf().disable();
    http.logout().disable();
    http.cors().configurationSource(configurationSource());
  }

}
