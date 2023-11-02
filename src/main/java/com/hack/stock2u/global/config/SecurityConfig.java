package com.hack.stock2u.global.config;

import static com.hack.stock2u.constant.UserRole.*;

import com.hack.stock2u.authentication.service.AuthAccessDeniedHandler;
import com.hack.stock2u.authentication.service.AuthEntryPoint;
import com.hack.stock2u.authentication.service.AuthManager;
import com.hack.stock2u.authentication.service.UserDetailService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableRedisHttpSession
public class SecurityConfig {
  private final UserDetailService userDetailService;
  private final AuthManager authManager;
  private final AuthEntryPoint authEntryPoint;
  private final AuthAccessDeniedHandler accessDeniedHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    disableMvcSettings(http);
    http.userDetailsService(userDetailService);
    http.authenticationManager(authManager);
    http.exceptionHandling()
        .accessDeniedHandler(accessDeniedHandler)
        .authenticationEntryPoint(authEntryPoint);

    http.authorizeRequests()
            .antMatchers(
                "/auth/**",
                "/docs/**", "/api-docs/**", "/swagger-ui/**",
                "/ws/**", "/stomp/**"
            ).permitAll()
            .antMatchers("/auth/withdraw", "/auth/logout")
            .authenticated()
            .antMatchers("/test/admin").hasRole(ADMIN.name())
            .anyRequest().authenticated();

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
    config.setAllowCredentials(true);
    config.addAllowedHeader("*");
    config.setAllowedOrigins(List.of(
        "http://localhost:3000",
        "https://localhost:3000",
        "https://stock2u-front.vercel.app/"
    ));
    config.setExposedHeaders(List.of("*"));
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
