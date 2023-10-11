package com.hack.stock2u.authentication.api;

import com.hack.stock2u.authentication.dto.AuthRequestDto;
import com.hack.stock2u.authentication.dto.LoginResponse;
import com.hack.stock2u.authentication.dto.UrlJson;
import com.hack.stock2u.authentication.service.AuthService;
import com.hack.stock2u.constant.AuthVendor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthApi {
  private final AuthService authService;

  @Operation(summary = "로그인 URL API", description = "각 벤더 요청에 따른 로그인 URL 을 반환합니다.")
  @GetMapping("/login-url")
  public ResponseEntity<UrlJson> login(
      @Parameter(
          name = "vendor",
          description = "외부 인증(OAuth 2.0) 을 담당하는 인증사 벤더 이름을 작성합니다.(google, kakao, naver)",
          required = true
      )
      @RequestParam("vendor") AuthVendor vendor
  ) {
    UrlJson loginUrl = authService.getLoginUrl(vendor);
    return ResponseEntity.ok(loginUrl);
  }

  @Operation(summary = "로그인 API", description = "로그인을 수행합니다.")
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> getTokenApi(@RequestBody AuthRequestDto.Token tokenRequest) {
    String token = tokenRequest.token();
    LoginResponse login = authService.login(token);
    return ResponseEntity.ok().body(login);
  }

}
