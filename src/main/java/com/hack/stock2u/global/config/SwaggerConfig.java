package com.hack.stock2u.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "Stock2U API 문서",
        description = "잔여 재고 공유 애플리케이션 API 명세",
        contact = @Contact(name = "최은기", email = "deveungi@gmail.com", url = "https://github.com/galaxy4276")
    )
)
@Configuration
public class SwaggerConfig {
}
