package com.hack.stock2u.authentication.service;

import com.hack.stock2u.authentication.AuthException;
import com.hack.stock2u.constant.AuthVendor;
import com.hack.stock2u.global.exception.GlobalException;
import com.hack.stock2u.models.User;
import com.hack.stock2u.user.repository.JpaUserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignupValidator {
  private final JpaUserRepository userRepository;
  private final AuthCodeProvider authCodeProvider;

  public void validReadyForSignup(String verification, String phone) {
    boolean readySignup = authCodeProvider.isReadySignup(verification);
    if (!readySignup) {
      throw AuthException.EXPIRED_SIGNUP.create();
    }
    validPhoneCheck(phone);
  }

  public void validPhoneCheck(String phone) {
    boolean authCodeComplete = authCodeProvider.checkVerifyComplete(phone);
    if (!authCodeComplete) {
      throw AuthException.INCOMPLETE_AUTH_CODE.create();
    }
  }

  public void validExistsUser(String email, AuthVendor vendor) {
    Optional<User> user = userRepository.findByEmailAndVendor(email, vendor);
    if (user.isPresent()) {
      throw AuthException.ALREADY_EXISTS_USER.create();
    }
  }

}
