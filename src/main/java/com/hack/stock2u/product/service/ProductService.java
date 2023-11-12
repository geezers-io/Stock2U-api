package com.hack.stock2u.product.service;

import com.hack.stock2u.authentication.service.SessionManager;
import com.hack.stock2u.chat.repository.JpaReservationRepository;
import com.hack.stock2u.file.repository.JpaAttachRepository;
import com.hack.stock2u.global.dto.GlobalResponse;
import com.hack.stock2u.global.exception.GlobalException;
import com.hack.stock2u.models.Attach;
import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.ProductImage;
import com.hack.stock2u.models.Reservation;
import com.hack.stock2u.models.User;
import com.hack.stock2u.product.dto.MainProductSet;
import com.hack.stock2u.product.dto.ProductCondition;
import com.hack.stock2u.product.dto.ProductCountProjection;
import com.hack.stock2u.product.dto.ProductDetails;
import com.hack.stock2u.product.dto.ProductRequest;
import com.hack.stock2u.product.dto.ProductSummaryProjection;
import com.hack.stock2u.product.exception.ProductException;
import com.hack.stock2u.product.repository.JpaProductImageRepository;
import com.hack.stock2u.product.repository.JpaProductRepository;
import com.hack.stock2u.user.dto.SellerDetails;
import com.hack.stock2u.user.repository.JpaSubscriptionRepository;
import com.hack.stock2u.user.service.SellerService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
  private final JpaProductRepository productRepository;
  private final JpaAttachRepository attachRepository;
  private final SessionManager sessionManager;
  private final SellerService sellerService;
  private final JpaSubscriptionRepository subscriptionRepository;
  private final JpaReservationRepository reservationRepository;

  // TODO: AI 기반 서칭, 마감 임박 순, 인근 위치 순 구현하기
  public MainProductSet getMainProductSet(ProductCondition condition, PageRequest pageable) {
    List<ProductSummaryProjection> products = getProducts(condition, pageable).getContent();
    return new MainProductSet(products, products, products);
  }

  public Page<ProductSummaryProjection> getProducts(ProductCondition condition, Pageable pageable) {
    log.debug("getProduct condition: {}", condition);
    Long totalCount = getCount(condition);
    log.debug("totalCount: {}", totalCount);
    List<ProductSummaryProjection> products = productRepository.findProducts(
        condition.getLatitude(),
        condition.getLongitude(),
        condition.getCategory(),
        condition.getDistance(),
        condition.getMinPrice(),
        condition.getMaxPrice(),
        pageable.getPageSize(),
        pageable.getOffset()
    );
    return new PageImpl<>(products, pageable, totalCount);
  }

  public ProductDetails getProductDetails(Long id) {
    Long purchaserId = sessionManager.getSessionUser().id();
    Product p = getProduct(id);
    User seller = p.getSeller();
    SellerDetails sellerDetails = sellerService.getSellerDetails(seller);
    List<Attach> images = attachRepository.findByProduct(p);

    Optional<Reservation> reservation = reservationRepository.findByProduct(p);

    boolean isSubscribe = subscriptionRepository.findBySubscriberId(purchaserId).isPresent();

    return ProductDetails.create(p, sellerDetails, images, isSubscribe, reservation);
  }

  public GlobalResponse.Id create(ProductRequest.Create createRequest) {
    validate(createRequest);

    User u = sessionManager.getSessionUserByRdb();
    Product product = Product.fromRequest(createRequest, u);
    List<Attach> images = getImages(createRequest, product);
    product.setAttaches(images);

    productRepository.save(product);
    return new GlobalResponse.Id(product.getId());
  }

  public void remove(Long id) {
    Product p = getProduct(id);
    p.remove();
    productRepository.save(p);
  }

  @Transactional
  public ProductDetails update(Long id, ProductRequest.Create updateRequest) {
    validate(updateRequest);
    Long purchaserId = sessionManager.getSessionUser().id();
    Product product = getProduct(id);
    List<Attach> images = getImages(updateRequest, product);
    User user = product.getSeller();

    product.update(updateRequest, images);
    productRepository.save(product);
    SellerDetails sellerDetails = sellerService.getSellerDetails(user);
    boolean isSubscribe = subscriptionRepository.findBySubscriberId(purchaserId).isPresent();

    return ProductDetails.create(
        product,
        sellerDetails,
        images,
        isSubscribe,
        Optional.empty()
    );
  }

  protected Attach getAttachById(Long id) {
    return attachRepository.findById(id)
        .orElseThrow(GlobalException.NOT_FOUND::create);
  }

  public Attach getProductAttach(Product p) {
    List<ProductImage> productImages = p.getProductImages();
    if (productImages.size() == 0) {
      return null;
    }
    return productImages.get(0).getAttach();
  }

  public List<Attach> getProductAttaches(Product p) {
    return p.getProductImages().stream()
        .map(ProductImage::getAttach)
        .toList();
  }
  
  protected List<Attach> getImages(ProductRequest.Create request, Product p) {
    List<Attach> attaches = request.imageIds().stream().map(this::getAttachById).toList();
    attaches.forEach(image -> image.setProduct(p));
    return attaches;
  }

  protected Product getProduct(Long id) {
    return productRepository.findById(id).orElseThrow(GlobalException.NOT_FOUND::create);
  }

  private void validate(ProductRequest.Create req) {
    if (req.imageIds().size() > 5) {
      throw ProductException.FILE_UPLOAD_LIMIT.create();
    }
  }

  private Long getCount(ProductCondition condition) {
    log.debug("get MinPrice: {}", condition.getMinPrice());
    ProductCountProjection ret = productRepository.getCount(
        condition.getLatitude(),
        condition.getLongitude(),
        condition.getCategory(),
        condition.getDistance(),
        condition.getMinPrice(),
        condition.getMaxPrice()
    );
    if (ret == null) {
      return 0L;
    }
    log.debug("totalCount: {} distance:{}", ret.getTotalCount(), ret.getDistance());

    return ret.getTotalCount();
  }

}
