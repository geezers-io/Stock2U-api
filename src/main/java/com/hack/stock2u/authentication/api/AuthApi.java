package com.hack.stock2u.authentication.api;

import com.hack.stock2u.authentication.dto.AuthRequestDto;
import com.hack.stock2u.authentication.dto.LoginResponse;
import com.hack.stock2u.authentication.dto.UrlJson;
import com.hack.stock2u.authentication.dto.doro.DoroSearchResponse;
import com.hack.stock2u.authentication.service.AuthService;
import com.hack.stock2u.authentication.service.RoadNameAddressService;
import com.hack.stock2u.constant.AuthVendor;
import com.hack.stock2u.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthApi {
  private final AuthService authService;
  private final RoadNameAddressService addressService;

  @Operation(summary = "로그인 URL API", description = "각 벤더 요청에 따른 로그인 URL 을 반환합니다.")
  @GetMapping("/login-url")
  public ResponseEntity<UrlJson> login(
      @Parameter(
          name = "vendor",
          description = "외부 인증(OAuth 2.0) 을 담당하는 인증사 벤더 이름을 작성합니다.(GOOGLE, KAKAO, NAVER)",
          required = true,
          example = "KAKAO"
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

  @Operation(summary = "일반 사용자 회원가입 API", description = "일반 사용자 회원가입을 수행합니다.")
  @PostMapping("/signup/user")
  public ResponseEntity<User> signupUserApi(
      @RequestBody AuthRequestDto.SignupUserRequest signupUserRequest
  ) {
    User user = authService.signupUser(signupUserRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  @Operation(summary = "회원가입 인증코드 발송 API", description = "사용자의 스마트폰으로 SMS 인증코드를 발송합니다.")
  @GetMapping("/code")
  public ResponseEntity<Void> sendAuthCodeApi(
      @Parameter(
          name = "phone",
          description = "대상자의 핸드폰 번호",
          required = true,
          example = "01012341234"
      )
      @RequestParam("phone") String phone
  ) {
    authService.sendCode(phone);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(
      summary = "회원가입 인증코드 검증 API",
      description = "발송된 회원 인증코드를 검증합니다. 해당 API 에서 검증 이후 회원가입이 가능합니다."
  )
  @PostMapping("/code/verify")
  public ResponseEntity<Void> verifyAuthCodeApi(
      @RequestBody AuthRequestDto.AuthCode authCodeRequest
  ) {
    String authCode = authCodeRequest.authCode();
    String phone = authCodeRequest.phone();

    authService.verifyCode(phone, authCode);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(
      summary = "도로명 주소 조회 API",
      description = "주소 검색 기반으로 도로명 주소 리스트를 조회합니다."
  )
  @GetMapping("/address")
  public ResponseEntity<DoroSearchResponse> getAddressApi(
      @Parameter(
          name = "keyword",
          description = "검색할 주소",
          required = true,
          example = "송도문화로"
      )
      @RequestParam("keyword") String keyword,
      @Parameter(
          name = "page",
          description = "조회 할 페이지 번호 (1부터 시작 0은 없음)",
          example = "1"
      )
      @RequestParam(value = "page", required = false) Integer page,
      @Parameter(
          name = "size",
          description = "한 번에 가져올 데이터 갯수 (기본 10개. 최대 100개)",
          example = "10"
      )
      @RequestParam(value = "size", required = false) Integer size
  ) {
    DoroSearchResponse address = addressService.getAddress(keyword, page, size);
    return ResponseEntity.ok(address);
  }

}
