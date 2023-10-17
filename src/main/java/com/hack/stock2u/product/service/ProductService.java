package com.hack.stock2u.product.service;

import com.hack.stock2u.authentication.service.SessionManager;
import com.hack.stock2u.file.repository.JpaAttachRepository;
import com.hack.stock2u.global.exception.GlobalException;
import com.hack.stock2u.models.Attach;
import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.ProductImage;
import com.hack.stock2u.models.User;
import com.hack.stock2u.product.dto.ProductDetails;
import com.hack.stock2u.product.dto.ProductRequest;
import com.hack.stock2u.product.dto.SellerDetails;
import com.hack.stock2u.product.exception.ProductException;
import com.hack.stock2u.product.repository.JpaProductImageRepository;
import com.hack.stock2u.product.repository.JpaProductRepository;
import com.hack.stock2u.user.service.SellerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
  private final JpaProductRepository productRepository;
  private final JpaAttachRepository attachRepository;
  private final SessionManager sessionManager;
  private final JpaProductImageRepository piRepository;
  private final SellerService sellerService;

  @Transactional
  public ProductDetails create(ProductRequest.Create createRequest) {
    List<Long> fileIds = createRequest.fileIds();
    if (fileIds.size() > 5) {
      throw ProductException.FILE_UPLOAD_LIMIT.create();
    }

    User u = sessionManager.getSessionUserByRdb();
    int salesCount = sellerService.getSalesCount(u);
    SellerDetails sellerDetails = SellerDetails.create(u, salesCount, 0);

    Product product = Product.fromRequest(createRequest, u);
    List<Attach> attaches = fileIds.stream().map(this::getAttachById).toList();
    List<ProductImage> productImages =
        piRepository.saveAll(attaches.stream().map(o -> ProductImage.create(product, o)).toList());

    product.setProductImages(productImages);
    productRepository.save(product);

    return ProductDetails.create(product, sellerDetails, attaches);
  }

  private Attach getAttachById(Long id) {
    return attachRepository.findById(id)
        .orElseThrow(GlobalException.NOT_FOUND::create);
  }

}
