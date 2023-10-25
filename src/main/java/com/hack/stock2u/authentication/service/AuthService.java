package com.hack.stock2u.authentication.service;

import com.hack.stock2u.authentication.dto.AuthRequestDto;
import com.hack.stock2u.authentication.dto.SignInResponse;
import com.hack.stock2u.authentication.dto.TokenSet;
import com.hack.stock2u.authentication.dto.UrlJson;
import com.hack.stock2u.authentication.dto.UserDetails;
import com.hack.stock2u.authentication.service.client.AuthUserDetail;
import com.hack.stock2u.authentication.service.client.KakaoClient;
import com.hack.stock2u.authentication.service.strategy.SignInStrategyBranch;
import com.hack.stock2u.authentication.service.strategy.SignInUrlCreateStrategy;
import com.hack.stock2u.constant.AuthVendor;
import com.hack.stock2u.global.exception.GlobalException;
import com.hack.stock2u.models.User;
import com.hack.stock2u.user.UserException;
import com.hack.stock2u.user.repository.JpaUserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

  private final SignInStrategyBranch signInStrategyBranch;
  private final KakaoClient kakaoClient;
  private final JpaUserRepository userRepository;
  private final AuthCodeProvider authCodeProvider;
  private final AuthManager authManager;
  private final SignupValidator validator;
  private final SessionManager sessionManager;

  /**
   * AuthVendor 에 따른 각 Vendor 로그인을 수행하는 URL 을 반환합니다.
   */
  public UrlJson getLoginUrl(AuthVendor vendor) {
    SignInUrlCreateStrategy urlCreateStrategy = signInStrategyBranch.getStrategy(vendor);
    return new UrlJson(urlCreateStrategy.create());
  }

  /**
   * 인증 코드를 기반으로 Resource Server 에 토큰 정보를 갱신하고 사용자 정보를 조회하여 LoginResponse 객체를 반환합니다.
   */
  public SignInResponse signIn(String authCode) {
    TokenSet tokenSet = kakaoClient.getToken(authCode);
    AuthUserDetail userDetails = kakaoClient.getUserDetails(tokenSet.accessToken());
    String oauthId = String.valueOf(userDetails.id());
    Optional<User> userOptional = userRepository.findByOauthId(oauthId);

    String email = userDetails.email();
    if (!userOptional.isPresent()) {
      authCodeProvider.saveAvailableSignup(oauthId);
      return SignInResponse.builder()
          .exists(false)
          .email(email)
          .verification(oauthId)
          .user(null)
          .build();
    }

    User user = userOptional.get();
    processLogin(user);
    return SignInResponse.builder()
        .exists(true)
        .email(email)
        .verification(oauthId)
        .user(UserDetails.user(user))
        .build();
  }

  public void withdraw(String reason) {
    User user = sessionManager.getSessionUserByRdb();
    user.remove(reason);
    userRepository.save(user);
  }

  /**
   * 구매자 회원가입 요청을 처리합니다.
   */
  @Transactional
  public UserDetails signupPurchaser(AuthRequestDto.SignupPurchaserRequest signupUserRequest) {
    validator.validReadyForSignup(signupUserRequest.verification(), signupUserRequest.phone());
    AuthVendor vendor = signupUserRequest.vendor();

    validator.validExistsUser(signupUserRequest.email(), vendor);

    User newUser = userRepository.save(User.signupPurchaser(signupUserRequest));
    processLogin(newUser);

    return UserDetails.user(newUser);
  }

  public UserDetails signupSeller(AuthRequestDto.SignupSellerRequest signupSellerRequest) {
    validator.validReadyForSignup(signupSellerRequest.verification(), signupSellerRequest.phone());
    String email = signupSellerRequest.email();
    AuthVendor vendor = signupSellerRequest.vendor();

    validator.validExistsUser(email, vendor);

    User newUser = userRepository.save(User.signupSeller(signupSellerRequest));
    processLogin(newUser);

    return UserDetails.user(newUser);
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

}
