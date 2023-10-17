package com.hack.stock2u.user.service;

import com.hack.stock2u.authentication.service.SessionManager;
import com.hack.stock2u.models.SellerDetails;
import com.hack.stock2u.models.User;
import com.hack.stock2u.user.dto.SellerResponse;
import com.hack.stock2u.user.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SellerService {
  private final JpaUserRepository userRepository;
  private final SessionManager sessionManager;

  public SellerResponse.BankDetails getBankDetails() {
    SellerDetails details = sessionManager.getSessionUser().getSellerDetails();
    return new SellerResponse.BankDetails(details.getBankName(), details.getAccount());
  }

}
