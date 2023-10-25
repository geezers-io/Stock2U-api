package com.hack.stock2u.user.api;

import com.hack.stock2u.constant.UserRole;
import com.hack.stock2u.user.dto.PurchaserSummary;
import com.hack.stock2u.user.dto.SellerSubscribeDetails;
import com.hack.stock2u.user.service.PurchaserService;
import com.hack.stock2u.user.service.SubscriptionService;
import com.hack.stock2u.utils.RoleGuard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "M. 마이페이지 - 구매자 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/my/purchaser")
public class PurchaserApi {
  private final PurchaserService userService;
  private final SubscriptionService subscriptionService;

  @RoleGuard(roles = UserRole.PURCHASER)
  @Operation(summary = "구매자 계정 정보 조회 API")
  @GetMapping
  public ResponseEntity<PurchaserSummary> getDetailsApi() {
    PurchaserSummary details = userService.getDetails();
    return ResponseEntity.ok(details);
  }

  @RoleGuard(roles = UserRole.PURCHASER)
  @Operation(summary = "구독 판매자 정보 리스트 조회 API")
  @GetMapping("/subscribe/list")
  public ResponseEntity<Page<SellerSubscribeDetails>> getSubscribeListApi(
      @Parameter(description = "조회할 페이지 넘버(0부터 시작)", required = true)
      @RequestParam("page") int page,
      @Parameter(description = "가져올 데이터 갯수 단위", required = true)
      @RequestParam("size") int size
  ) {
    PageRequest pageable = PageRequest.of(page, size);
    Page<SellerSubscribeDetails> ret =
        subscriptionService.getSubscriberDetailsList(pageable);
    return ResponseEntity.status(HttpStatus.OK).body(ret);
  }

  @RoleGuard(roles = UserRole.PURCHASER)
  @Operation(summary = "판매자 구독 API")
  @PostMapping("/subscribe")
  public ResponseEntity<Void> subscribeApi(
      @Schema(description = "판매자 ID", required = true)
      @RequestParam("id") Long sellerId
  ) {
    subscriptionService.subscribe(sellerId);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @RoleGuard(roles = UserRole.PURCHASER)
  @Operation(summary = "판매자 구독 취소 API")
  @DeleteMapping("/unsubscribe")
  public ResponseEntity<Void> unsubscribeApi(
      @Schema(description = "판매자 ID", required = true)
      @RequestParam("id") Long sellerId
  ) {
    subscriptionService.unsubscribe(sellerId);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

}
