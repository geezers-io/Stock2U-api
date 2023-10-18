package com.hack.stock2u.product.api;

import com.hack.stock2u.constant.UserRole;
import com.hack.stock2u.product.dto.ProductDetails;
import com.hack.stock2u.product.dto.ProductRequest;
import com.hack.stock2u.product.service.ProductService;
import com.hack.stock2u.utils.RoleGuard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "P. 잔여 재고 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductApi {
  private final ProductService productService;

  @Operation(summary = "잔여 재고 게시글 조회 API")
  @GetMapping("/{id}")
  public ResponseEntity<ProductDetails> getProductApi(@PathVariable("id") Long id) {
    ProductDetails productDetails = productService.getProductDetails(id);
    return ResponseEntity.status(HttpStatus.OK).body(productDetails);
  }

  @RoleGuard(roles = UserRole.SELLER)
  @Operation(summary = "잔여 재고 게시글 생성 API", description = "잔여 재고 게시글을 생성하고 생성된 객체를 반환합니다.")
  @PostMapping
  public ResponseEntity<ProductDetails> createProductApi(
      @RequestBody @Valid ProductRequest.Create createRequest
  ) {
    ProductDetails productDetails = productService.create(createRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(productDetails);
  }

  @RoleGuard(roles = UserRole.SELLER)
  @Operation(summary = "잔여 재고 게시글 수정 API", description = "잔여 재고 게시글을 수정하고 수정된 객체를 반환합니다.")
  @PutMapping("/{id}")
  public ResponseEntity<ProductDetails> updateProductApi(
      @Schema(description = "게시글 id", required = true)
      @PathVariable("id") Long id,
      @RequestBody @Valid ProductRequest.Create updateRequest
  ) {
    ProductDetails productDetails = productService.update(id, updateRequest);
    return ResponseEntity.status(HttpStatus.OK).body(productDetails);
  }

  @RoleGuard(roles = {UserRole.ADMIN, UserRole.SELLER})
  @Operation(summary = "잔여 재고 게시글 삭제 API")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> removeProductApi(@PathVariable("id") Long id) {
    productService.remove(id);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

}
