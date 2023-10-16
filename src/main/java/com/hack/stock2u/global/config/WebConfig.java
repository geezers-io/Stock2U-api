package com.hack.stock2u.global.config;

import com.hack.stock2u.authentication.service.AuthVendorEnumConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new AuthVendorEnumConverter());
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedHeaders("*")  //모든 Header 허용
        .allowedOriginPatterns("*") //모든 Origin 허용
        .allowedMethods("*") //모든 Method 허용
        .allowCredentials(true) // Credentials 허용
        .exposedHeaders("Content-Disposition");
  }
}
