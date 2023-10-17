package com.hack.stock2u.user.service;

import com.hack.stock2u.authentication.service.AuthCodeProvider;
import com.hack.stock2u.authentication.service.SessionManager;
import com.hack.stock2u.models.User;
import com.hack.stock2u.user.dto.PurchaserRequest;
import com.hack.stock2u.user.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PurchaserService {
  private final JpaUserRepository userRepository;
  private final SessionManager sessionManager;
  private final AuthCodeProvider authCodeProvider;

  public void update(PurchaserRequest.Update updateRequest) {
    authCodeProvider.verifyCode(updateRequest.phone(), updateRequest.authCode());
    User user = sessionManager.getSessionUser();
    user.changePhone(updateRequest.phone());
    userRepository.save(user);
  }

}
