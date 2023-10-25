package com.hack.stock2u.global.config;

import com.hack.stock2u.authentication.service.AuthVendorEnumConverter;
import com.hack.stock2u.global.filter.RoleGuardInterceptor;
import com.hack.stock2u.utils.ProductTypeEnumConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
  private final RoleGuardInterceptor roleGuardInterceptor;

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new AuthVendorEnumConverter());
    registry.addConverter(new ProductTypeEnumConverter());
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(roleGuardInterceptor);
  }
}
