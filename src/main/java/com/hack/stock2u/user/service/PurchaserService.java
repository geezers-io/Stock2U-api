package com.hack.stock2u.user.service;

import com.hack.stock2u.authentication.dto.SessionUser;
import com.hack.stock2u.authentication.service.SessionManager;
import com.hack.stock2u.user.dto.PurchaserSummary;
import com.hack.stock2u.user.dto.UserId;
import com.hack.stock2u.user.repository.JpaUserRepository;
import com.hack.stock2u.user.repository.PurchaserDslRepository;
import com.hack.stock2u.user.repository.UserDslRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PurchaserService {
  private final JpaUserRepository userRepository;
  private final SessionManager sessionManager;
  private final PurchaserDslRepository purchaserRepository;
  private final UserDslRepository userDslRepository;


  public PurchaserSummary getDetails() {
    SessionUser user = sessionManager.getSessionUser();
    String avatarUrl = userDslRepository.getAvatarUrl(new UserId(user.id()));
    Long buyCount = purchaserRepository.getBuyCount(user.id());

    return PurchaserSummary.builder()
        .id(user.id())
        .name(user.username())
        .phone(user.phone())
        .avatarUrl(avatarUrl)
        .buyCount(buyCount.intValue())
        .reviewCount(0)
        .build();
  }

}
