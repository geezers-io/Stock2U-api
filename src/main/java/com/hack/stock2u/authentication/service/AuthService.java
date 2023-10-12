package com.hack.stock2u.authentication.service;

import com.hack.stock2u.authentication.dto.LoginResponse;
import com.hack.stock2u.authentication.dto.TokenSet;
import com.hack.stock2u.authentication.dto.UrlJson;
import com.hack.stock2u.authentication.service.client.AuthUserDetail;
import com.hack.stock2u.authentication.service.client.KakaoClient;
import com.hack.stock2u.authentication.service.strategy.LoginStrategyBranch;
import com.hack.stock2u.authentication.service.strategy.LoginUrlCreateStrategy;
import com.hack.stock2u.constant.AuthVendor;
import com.hack.stock2u.models.User;
import com.hack.stock2u.user.repository.JpaUserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

  private final LoginStrategyBranch loginStrategyBranch;
  private final KakaoClient kakaoClient;
  private final JpaUserRepository userRepository;
  private final AuthenticationManager authManager;

  /**
   * AuthVendor 에 따른 각 Vendor 로그인을 수행하는 URL 을 반환합니다.
   */
  public UrlJson getLoginUrl(AuthVendor vendor) {
    LoginUrlCreateStrategy urlCreateStrategy = loginStrategyBranch.getStrategy(vendor);
    return new UrlJson(urlCreateStrategy.create());
  }

  /**
   * 인증 코드를 기반으로 Resource Server 에 토큰 정보를 갱신하고 사용자 정보를 조회하여 LoginResponse 객체를 반환합니다.
   */
  public LoginResponse login(String authCode) {
    TokenSet tokenSet = kakaoClient.getToken(authCode);
    AuthUserDetail userDetails = kakaoClient.getUserDetails(tokenSet.accessToken());
    Optional<User> userOptional = userRepository.findByEmail(userDetails.email());

    boolean exists = userOptional.isPresent();
    String email = userDetails.email();
    if (!exists) {
      return new LoginResponse(false, email);
    }
    User user = userOptional.get();
    processLogin(user);
    return new LoginResponse(true, email);
  }

  public void signup() {}

  private void processLogin(User user) {
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
        user.getId(), user.getEmail()
    );
    Authentication authenticate = authManager.authenticate(token);
    SecurityContextHolder.getContext().setAuthentication(authenticate);
  }

}
