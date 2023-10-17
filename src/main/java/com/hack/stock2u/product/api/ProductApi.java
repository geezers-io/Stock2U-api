package com.hack.stock2u.product.api;

import com.hack.stock2u.product.dto.ProductDetails;
import com.hack.stock2u.product.dto.ProductRequest;
import com.hack.stock2u.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "P. 잔여 재고 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductApi {
  private final ProductService productService;

  @Operation(summary = "잔여 재고 게시글 생성 API", description = "잔여 재고 게시글을 생성하고 생성된 객체를 반환합니다.")
  @PostMapping
  public ResponseEntity<ProductDetails> createProductApi(
      @RequestBody @Valid ProductRequest.Create createRequest
  ) {
    ProductDetails productDetails = productService.create(createRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(productDetails);
  }
}
