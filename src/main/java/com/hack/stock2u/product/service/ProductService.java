package com.hack.stock2u.product.service;

import com.hack.stock2u.authentication.service.SessionManager;
import com.hack.stock2u.file.repository.JpaAttachRepository;
import com.hack.stock2u.global.exception.GlobalException;
import com.hack.stock2u.models.Attach;
import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.ProductImage;
import com.hack.stock2u.models.User;
import com.hack.stock2u.product.dto.ProductDetails;
import com.hack.stock2u.product.dto.ProductImageSet;
import com.hack.stock2u.product.dto.ProductRequest;
import com.hack.stock2u.product.exception.ProductException;
import com.hack.stock2u.product.repository.JpaProductImageRepository;
import com.hack.stock2u.product.repository.JpaProductRepository;
import com.hack.stock2u.user.dto.SellerDetails;
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

  public ProductDetails getProductDetails(Long id) {
    Product p = getProduct(id);
    User u = p.getSeller();
    SellerDetails sellerDetails = sellerService.getSellerDetails(u);
    List<Attach> attaches = p.getProductImages().stream()
        .map(ProductImage::getAttach)
        .toList();
    return ProductDetails.create(p, sellerDetails, attaches);
  }

  @Transactional
  public ProductDetails create(ProductRequest.Create createRequest) {
    validate(createRequest);

    User u = sessionManager.getSessionUserByRdb();
    SellerDetails sellerDetails = sellerService.getSellerDetails(u);
    Product product = Product.fromRequest(createRequest, u);
    ProductImageSet set = getImageSet(createRequest, product);
    product.setProductImages(set.productImages());
    productRepository.save(product);

    return ProductDetails.create(product, sellerDetails, set.attaches());
  }

  public void remove(Long id) {
    Product p = getProduct(id);
    p.remove();
    productRepository.save(p);
  }

  @Transactional
  public ProductDetails update(Long id, ProductRequest.Create updateRequest) {
    validate(updateRequest);
    Product p = getProduct(id);
    ProductImageSet set = getImageSet(updateRequest, p);
    User u = p.getSeller();

    p.update(updateRequest, set.productImages());
    productRepository.save(p);
    SellerDetails sellerDetails = sellerService.getSellerDetails(u);

    return ProductDetails.create(p, sellerDetails, set.attaches());
  }

  protected Attach getAttachById(Long id) {
    return attachRepository.findById(id)
        .orElseThrow(GlobalException.NOT_FOUND::create);
  }

  protected ProductImageSet getImageSet(ProductRequest.Create request, Product p) {
    List<Attach> attaches = request.fileIds().stream().map(this::getAttachById).toList();
    List<ProductImage> productImages =
        piRepository.saveAll(attaches.stream().map(o -> ProductImage.create(p, o)).toList());
    return new ProductImageSet(attaches, productImages);
  }


  protected Product getProduct(Long id) {
    return productRepository.findById(id).orElseThrow(GlobalException.NOT_FOUND::create);
  }

  private void validate(ProductRequest.Create req) {
    if (req.fileIds().size() > 5) {
      throw ProductException.FILE_UPLOAD_LIMIT.create();
    }
  }

}
