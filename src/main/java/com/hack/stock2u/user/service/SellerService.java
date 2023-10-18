package com.hack.stock2u.user.service;

import com.hack.stock2u.authentication.service.SessionManager;
import com.hack.stock2u.models.SellerDetails;
import com.hack.stock2u.models.User;
import com.hack.stock2u.user.dto.SellerResponse;
import com.hack.stock2u.user.repository.JpaBuyerRepository;
import com.hack.stock2u.user.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SellerService {
  private final JpaUserRepository userRepository;
  private final JpaBuyerRepository buyerRepository;
  private final SessionManager sessionManager;

  public SellerResponse.BankDetails getBankDetails() {
    SellerDetails details = sessionManager.getSessionUserByRdb().getSellerDetails();
    return new SellerResponse.BankDetails(details.getBankName(), details.getAccount());
  }

  public int getSalesCount(User u) {
    return buyerRepository.countBySeller(u);
  }

  // FIX: 리뷰 미구현으로 갯수 0 고정
  public com.hack.stock2u.user.dto.SellerDetails getSellerDetails(User u) {
    int salesCount = getSalesCount(u);
    return com.hack.stock2u.user.dto.SellerDetails.create(u, salesCount, 0);
  }

}
