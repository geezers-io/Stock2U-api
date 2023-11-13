package com.hack.stock2u.user.api;

import com.hack.stock2u.constant.UserRole;
import com.hack.stock2u.product.exception.ProductException;
import com.hack.stock2u.user.dto.SellerManagementSummary;
import com.hack.stock2u.user.dto.SellerRequest;
import com.hack.stock2u.user.dto.SellerResponse;
import com.hack.stock2u.user.dto.SellerSummary;
import com.hack.stock2u.user.service.SellerService;
import com.hack.stock2u.user.validator.BankName;
import com.hack.stock2u.utils.RoleGuard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "M. 마이페이지 - 판매자 API")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/my/seller")
public class SellerApi {
  private final SellerService sellerService;

  @RoleGuard(roles = UserRole.SELLER)
  @Operation(summary = "판매자 계정 정보 조회 API")
  @GetMapping
  public ResponseEntity<SellerSummary> getDetailsApi() {
    SellerSummary details = sellerService.getDetails();
    return ResponseEntity.ok(details);
  }

  @RoleGuard(roles = UserRole.SELLER)
  @Operation(summary = "판매자 은행정보(은행이름/계좌) 조회 API", description = "판매자 은행이름과 계좌정보를 조회합니다.")
  @GetMapping("bank-details")
  public ResponseEntity<SellerResponse.BankDetails> getBankDetailsApi() {
    SellerResponse.BankDetails bankDetails = sellerService.getBankDetails();
    return ResponseEntity.status(HttpStatus.OK).body(bankDetails);
  }

  @RoleGuard(roles = UserRole.SELLER)
  @Operation(
      summary = "판매자 소재지(도로명주소) 변경 API",
      description = "도로명 주소 API 를 사용하여 반환된 값으로 판매자의 소재지를 수정합니다."
  )
  @PatchMapping("/location")
  public ResponseEntity<Void> updateLocationApi(
      @RequestBody @Valid SellerRequest.LocationUpdate locationUpdateRequest
  ) {
    sellerService.updateLocation(locationUpdateRequest);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @RoleGuard(roles = UserRole.SELLER)
  @Operation(
      summary = "판매자 은행정보 변경 API",
      description = "은행 정보 API 에서 제공하는 이름과 계좌번호로 판매자의 은행정보를 수정합니다."
  )
  @PatchMapping("/bank")
  public ResponseEntity<Void> updateBank(
      @Schema(description = "은행 이름(은행 정보 조회 API)", required = true)
      @BankName
      @RequestParam("bankName") String bankName,
      @Schema(description = "계좌번호", required = true)
      @RequestParam("account") String account
  ) {
    sellerService.updateBank(bankName, account);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @RoleGuard(roles = UserRole.SELLER)
  @Operation(
      summary = "판매자 판매관리 API",
      description = "판매자가 올린 게시글을 조회하여 상세이동, 예약 확인, 삭제에 기능을 수행합니다."
  )
  @GetMapping("/management")
  public ResponseEntity<Page<SellerManagementSummary>> getManagementsApi(
      @Parameter(description = "게시글 제목으로 검색")
      @RequestParam(value = "title", required = false)
      String title,
      @Parameter(description = "예약 상품 보기")
      @RequestParam(value = "isReservedProduct", defaultValue = "false")
      boolean isReservedProduct,
      @Parameter(description = "판매 완료 상품 보기")
      @RequestParam(value = "isCompletedProduct", defaultValue = "false")
      boolean isCompletedProduct,
      @Parameter(description = "조회할 페이지 넘버(0부터 시작)", required = true)
      @RequestParam("page") int page,
      @Parameter(description = "가져올 데이터 갯수 단위", required = true)
      @RequestParam("size") int size
  ) {
    if (isCompletedProduct && isReservedProduct) {
      throw ProductException.CANNOT_CHECK_BOTH.create();
    }
    PageRequest pageable = PageRequest.of(page, size);
    Page<SellerManagementSummary> summary =
        sellerService.getManagements(pageable, title, isReservedProduct, isCompletedProduct);

    return ResponseEntity.status(HttpStatus.OK).body(summary);
  }
}
