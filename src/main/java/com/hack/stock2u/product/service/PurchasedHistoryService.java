package com.hack.stock2u.product.service;

import com.hack.stock2u.authentication.dto.SessionUser;
import com.hack.stock2u.authentication.service.SessionManager;
import com.hack.stock2u.models.Buyer;
import com.hack.stock2u.models.Product;
import com.hack.stock2u.user.dto.PurchasedHistory;
import com.hack.stock2u.user.dto.SearchDate;
import com.hack.stock2u.user.repository.BuyerDslRepository;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PurchasedHistoryService {
  private final BuyerDslRepository buyerRepository;
  private final SessionManager sessionManager;

  public Page<PurchasedHistory> getHistories(SearchDate searchDate, Pageable pageable) {
    SessionUser user = sessionManager.getSessionUser();
    List<Buyer> buyers = buyerRepository.findByPurchaserId(
        user.id(),
        searchDate.start(),
        searchDate.end(), // search 2,
        pageable
    );

    List<PurchasedHistory> histories = buyers.stream()
        .map(this::mapToHistory).toList();

    return new PageImpl<>(histories, pageable, histories.size());
  }

  private PurchasedHistory mapToHistory(Buyer b) {
    Product product = b.getProduct();
    return PurchasedHistory.builder()
        .productId(product.getId())
        .title(product.getTitle())
        .name(product.getName())
        .thumbnailUrl("")
        .createdAt(new LocalDateTime().toDate())
        .build();
  }

}
