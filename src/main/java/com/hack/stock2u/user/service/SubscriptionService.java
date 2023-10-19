package com.hack.stock2u.user.service;

import com.hack.stock2u.authentication.dto.SessionUser;
import com.hack.stock2u.authentication.service.SessionManager;
import com.hack.stock2u.file.dto.SimpleFile;
import com.hack.stock2u.global.exception.GlobalException;
import com.hack.stock2u.models.Attach;
import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.Subscription;
import com.hack.stock2u.models.User;
import com.hack.stock2u.product.dto.ProductDetails;
import com.hack.stock2u.product.dto.SimpleProduct;
import com.hack.stock2u.product.repository.JpaProductRepository;
import com.hack.stock2u.product.service.ProductService;
import com.hack.stock2u.user.UserException;
import com.hack.stock2u.user.dto.SellerDetails;
import com.hack.stock2u.user.dto.SellerSubscribeDetails;
import com.hack.stock2u.user.repository.JpaSubscriptionRepository;
import com.hack.stock2u.user.repository.JpaUserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SubscriptionService {
  private final JpaUserRepository userRepository;
  private final SessionManager sessionManager;
  private final JpaSubscriptionRepository subscriptionRepository;
  private final JpaProductRepository productRepository;
  private final ProductService productService;
  private final SellerService sellerService;

  public void subscribe(Long sellerId) {
    User purchaser = sessionManager.getSessionUserByRdb();
    User seller = userRepository.findById(sellerId)
        .orElseThrow(UserException.NOT_FOUND_USER::create);

    Subscription subscription = new Subscription(purchaser, seller);
    subscriptionRepository.save(subscription);
  }

  public void unsubscribe(Long sellerId) {
    SessionUser sessionUser = sessionManager.getSessionUser();
    Long subId = subscriptionRepository.findByUserIds(sessionUser.id(), sellerId)
        .orElseThrow(GlobalException.NOT_FOUND::create);
    subscriptionRepository.deleteById(subId);
  }

  public Page<SellerSubscribeDetails> getSubscriberDetailsList(Pageable pageable) {
    Long pid = sessionManager.getSessionUser().id();
    List<Subscription> subs = subscriptionRepository.findBySubscriberId(pid, pageable)
        .getContent();
    List<SellerSubscribeDetails> subscribeSellers = subs.stream()
        .map(Subscription::getTarget)
        .map(this::createSellerSubscribeDetails)
        .toList();

    return new PageImpl<>(subscribeSellers);
  }

  private SellerSubscribeDetails createSellerSubscribeDetails(User seller) {
    SellerDetails sellerDetails = sellerService.getSellerDetails(seller);
    List<Product> products = productRepository.findTop5BySeller(seller);
    List<SimpleProduct> currentProducts = products.stream()
        .map(this::createSimpleProduct)
        .toList();
    return new SellerSubscribeDetails(sellerDetails, currentProducts);
  }

  public SimpleProduct createSimpleProduct(Product p) {
    Attach attach = productService.getProductAttach(p);
    SimpleFile banner = SimpleFile.attach(attach);
    return new SimpleProduct(p.getId(), p.getTitle(), banner);
  }

}
