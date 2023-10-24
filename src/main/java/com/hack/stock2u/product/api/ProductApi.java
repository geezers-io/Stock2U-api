package com.hack.stock2u.product.api;

import static com.hack.stock2u.constant.ProductType.*;

import com.hack.stock2u.constant.ProductType;
import com.hack.stock2u.constant.UserRole;
import com.hack.stock2u.global.dto.GlobalResponse;
import com.hack.stock2u.product.dto.MainProductSet;
import com.hack.stock2u.product.dto.ProductCondition;
import com.hack.stock2u.product.dto.ProductDetails;
import com.hack.stock2u.product.dto.ProductRequest;
import com.hack.stock2u.product.dto.ProductSummaryProjection;
import com.hack.stock2u.product.service.ProductService;
import com.hack.stock2u.utils.RoleGuard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "P. 잔여 재고 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductApi {
  private final ProductService productService;


  @Operation(
      summary = "메인 페이지 게시글 목록 조회 API",
      description = "AI(GPT), 마감 순, 인근 위치 순 섹션대로 리스트를 조회합니다."
  )
  @GetMapping
  public ResponseEntity<MainProductSet> getProductsApi(
      @Schema(description = "사용자 위도", required = true, example = "33.450701")
      @RequestParam("latitude")
      Double latitude,
      @Schema(description = "사용자 경도", required = true, example = "126.570667")
      @RequestParam("longitude")
      Double longitude
  ) {
    ProductCondition condition = new ProductCondition();
    condition.setLatitude(latitude);
    condition.setLongitude(longitude);
    condition.setDistance(10.0);
    condition.setCategory(List.of(FOOD.name(), ACCOMMODATION.name(), TICKET.name()));
    condition.setMinPrice(0);
    condition.setMaxPrice(2147483647);
    PageRequest pageable = PageRequest.of(0, 8);

    MainProductSet set = productService.getMainProductSet(condition, pageable);
    return ResponseEntity.ok(set);
  }


  @Operation(summary = "게시글 목록 조회 API", description = "다양한 필터값을 기반으로 게시글 목록을 불러옵니다.")
  @GetMapping("/search")
  public ResponseEntity<Page<ProductSummaryProjection>> getProductListSearchApi(
      @Schema(description = "사용자 위도", required = true, example = "33.450701")
      @RequestParam("latitude")
      Double latitude,
      @Schema(description = "사용자 경도", required = true, example = "126.570667")
      @RequestParam("longitude")
      Double longitude,
      @Schema(description = "거리 필터(1, 3, 5, 10 중 하나로 부탁드려요)")
      @RequestParam(value = "distance", required = false)
      Double distance,
      @Schema(description = "재고 분류(다중 선택 가능)")
      @Parameter(
          name = "category",
          description = "조회할 재고 유형(리스트)",
          example = "FOOD"
      )
      @RequestParam(value = "category", required = false)
      List<ProductType> category,
      @Schema(description = "최소 금액")
      @RequestParam(value = "minPrice", required = false)
      Integer minPrice,
      @Schema(description = "최대 금액")
      @RequestParam(value = "maxPrice", required = false)
      Integer maxPrice,
      @Parameter(description = "조회할 페이지 넘버(0부터 시작)", required = true, example = "0")
      @RequestParam("page") int page,
      @Parameter(description = "가져올 데이터 갯수 단위", required = true, example = "10")
      @RequestParam("size") int size
  ) {
    ProductCondition condition = new ProductCondition();
    condition.setLatitude(latitude);
    condition.setLongitude(longitude);
    condition.setDistance(Objects.requireNonNullElse(distance, 10.0));
    condition.setCategory(
        Objects.requireNonNullElseGet(
            category,
            () -> List.of(FOOD, TICKET, ACCOMMODATION)
        ).stream().map(ProductType::name).toList()
    );
    condition.setMinPrice(Objects.requireNonNullElse(minPrice, 0));
    condition.setMaxPrice(Objects.requireNonNullElse(maxPrice, 2147483647));
    PageRequest pageRequest = PageRequest.of(page, size);

    Page<ProductSummaryProjection> products = productService.getProducts(condition, pageRequest);
    return ResponseEntity.ok(products);
  }

  @Operation(summary = "잔여 재고 게시글 조회 API")
  @GetMapping("/{id}")
  public ResponseEntity<ProductDetails> getProductApi(@PathVariable("id") Long id) {
    ProductDetails productDetails = productService.getProductDetails(id);
    return ResponseEntity.status(HttpStatus.OK).body(productDetails);
  }

  @RoleGuard(roles = UserRole.SELLER)
  @Operation(summary = "잔여 재고 게시글 생성 API", description = "잔여 재고 게시글을 생성하고 생성된 객체를 반환합니다.")
  @PostMapping
  public ResponseEntity<GlobalResponse.Id> createProductApi(
      @RequestBody @Valid ProductRequest.Create createRequest
  ) {
    GlobalResponse.Id id = productService.create(createRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(id);
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
