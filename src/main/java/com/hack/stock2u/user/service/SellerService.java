package com.hack.stock2u.user.service;

import com.hack.stock2u.authentication.service.SessionManager;
import com.hack.stock2u.models.SellerDetails;
import com.hack.stock2u.models.User;
import com.hack.stock2u.user.dto.AvatarId;
import com.hack.stock2u.user.dto.SellerRequest;
import com.hack.stock2u.user.dto.SellerResponse;
import com.hack.stock2u.user.dto.SellerSummary;
import com.hack.stock2u.user.repository.JpaBuyerRepository;
import com.hack.stock2u.user.repository.JpaUserRepository;
import com.hack.stock2u.user.repository.SellerDslRepository;
import com.hack.stock2u.user.repository.UserDslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SellerService {
  private final JpaUserRepository userRepository;
  private final JpaBuyerRepository buyerRepository;
  private final SessionManager sessionManager;
  private final SellerDslRepository sellerDslRepository;
  private final UserDslRepository userDslRepository;

  public SellerSummary getDetails() {
    User user = sessionManager.getSessionUserByRdb();
    SellerDetails sellerDetails = user.getSellerDetails();
    Long salesCount = sellerDslRepository.getSalesCount(user.getId());
    String avatarUrl = userDslRepository.getAvatarUrl(new AvatarId(user.getAvatarId()));

    return SellerSummary.builder()
        .id(user.getId())
        .phone(user.getPhone())
        .name(user.getName())
        .location(sellerDetails.getLocation())
        .bankDetails(sellerDetails.getBankName() + sellerDetails.getAccount())
        .reviewCount(0)
        .salesCount(salesCount.intValue())
        .avatarUrl(avatarUrl)
        .build();
  }

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

  public void updateLocation(SellerRequest.LocationUpdate locationUpdateRequest) {
    User u = sessionManager.getSessionUserByRdb();
    SellerDetails details = u.getSellerDetails();
    details.changeLocation(locationUpdateRequest.location());
    details.changePosition(locationUpdateRequest);
    u.changeSellerDetails(details);
    userRepository.save(u);
  }

  public void updateBank(String bankName, String account) {
    User u = sessionManager.getSessionUserByRdb();
    SellerDetails details = u.getSellerDetails();
    details.changeBankName(bankName);
    details.changeAccount(account);
    userRepository.save(u);
  }

}
