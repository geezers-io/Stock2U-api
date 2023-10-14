package com.hack.stock2u.authentication.service;

import com.hack.stock2u.authentication.AuthException;
import com.hack.stock2u.authentication.dto.AuthRequestDto;
import com.hack.stock2u.authentication.dto.LoginResponse;
import com.hack.stock2u.authentication.dto.TokenSet;
import com.hack.stock2u.authentication.dto.UrlJson;
import com.hack.stock2u.authentication.dto.UserDetails;
import com.hack.stock2u.authentication.service.client.AuthUserDetail;
import com.hack.stock2u.authentication.service.client.KakaoClient;
import com.hack.stock2u.authentication.service.strategy.LoginStrategyBranch;
import com.hack.stock2u.authentication.service.strategy.LoginUrlCreateStrategy;
import com.hack.stock2u.constant.AuthVendor;
import com.hack.stock2u.models.User;
import com.hack.stock2u.user.UserException;
import com.hack.stock2u.user.repository.JpaUserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

  private final LoginStrategyBranch loginStrategyBranch;
  private final KakaoClient kakaoClient;
  private final JpaUserRepository userRepository;
  private final AuthCodeProvider authCodeProvider;
  private final AuthManager authManager;

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
      return new LoginResponse(false, email, null);
    }
    User user = userOptional.get();
    processLogin(user);
    return new LoginResponse(true, email, UserDetails.user(user));
  }

  public void withdraw(String reason) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long userId = (Long) authentication.getCredentials();
    User user = userRepository.findById(userId).orElseThrow(UserException.NOT_FOUND_USER::create);
    user.remove(reason);
    userRepository.save(user);
  }

  /**
   * 일반 사용자 회원가입 요청을 처리합니다.
   */
  @Transactional
  public User signupUser(AuthRequestDto.SignupUserRequest signupUserRequest) {
    String phone = signupUserRequest.phone();
    AuthVendor vendor = signupUserRequest.vendor();

    validPhoneCheck(phone);
    Optional<User> user = userRepository.findByEmailAndVendor(signupUserRequest.email(), vendor);

    if (user.isPresent()) {
      log.warn("isPresnent: {}", user.isPresent());
      throw AuthException.ALREADY_EXISTS_USER.create();
    }

    User newUser = userRepository.save(User.signupUser(signupUserRequest));
    processLogin(newUser);

    return newUser;
  }

  /**
   * 인증코드를 생성하여 발송합니다.
   */
  public void sendCode(String phone) {
    authCodeProvider.save(phone, 10);
  }

  /**
   * 인증코드를 검사합니다.
   */
  public void verifyCode(String phone, String authCode) {
    authCodeProvider.verifyCode(phone, authCode);
  }

  private void processLogin(User user) {
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
        user.getPhone(), user.getId()
    );
    Authentication authenticate = authManager.authenticate(token);
    SecurityContextHolder.getContext().setAuthentication(authenticate);
  }

  private void validPhoneCheck(String phone) {
    boolean authCodeComplete = authCodeProvider.checkVerifyComplete(phone);
    if (!authCodeComplete) {
      throw AuthException.INCOMPLETE_AUTH_CODE.create();
    }
  }

}
