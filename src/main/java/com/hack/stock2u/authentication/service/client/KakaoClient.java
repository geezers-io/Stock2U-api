package com.hack.stock2u.authentication.service.client;

import com.hack.stock2u.authentication.AuthException;
import com.hack.stock2u.authentication.config.KakaoProperties;
import com.hack.stock2u.authentication.dto.TokenSet;
import com.hack.stock2u.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoClient {
  private final KakaoProperties properties;

  /**
   * AccessToken 과 RefreshToken 을 TokenSet 으로 반환합니다.
   */
  public TokenSet getToken(String authCode) {
    WebClient webClient = WebClient.create(createUri(authCode));
    KakaoTokenResponse tokenResponse = webClient
        .post()
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .retrieve()
        .onStatus(
            HttpStatus::is4xxClientError,
            res -> Mono.error(GlobalException.BAD_REQUEST.create())
        )
        .bodyToFlux(KakaoTokenResponse.class)
        .blockFirst();

    String accessToken = tokenResponse.getAccess_token();
    String refreshToken = tokenResponse.getRefresh_token();
    return new TokenSet(accessToken, refreshToken);
  }

  /**
   * 액세스 토큰 기반으로 사용자의 고유 번호(id) 와 email 값을 객체로 반환합니다.
   */
  public AuthUserDetail getUserDetails(String accessToken) {
    KakaoUserDetailsResponse userDetails = WebClient.create("https://kapi.kakao.com/v2/user/me")
        .post()
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .header("Authorization", "Bearer " + accessToken)
        .retrieve()
        .bodyToFlux(KakaoUserDetailsResponse.class)
        .blockFirst();

    return new AuthUserDetail(userDetails.id(), userDetails.kakao_account().email());
  }

  private String createUri(String authCode) {
    String target = "https://kauth.kakao.com/oauth/token?grant_type=%s&client_id=%s&redirect_uri=%s&client_secret=%s&code=%s";
    return String.format(
        target, "authorization_code", properties.getClientId(),
        properties.getRedirectUri(), properties.getClientSecret(), authCode
    );
  }

}
