package com.hack.stock2u.user.api;

import com.hack.stock2u.user.dto.SellerResponse;
import com.hack.stock2u.user.service.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "M. 마이페이지 - 판매자 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/my/seller")
public class SellerApi {
  private final SellerService sellerService;

  @Operation(summary = "판매자 은행정보(은행이름/계좌) 조회 API", description = "사업자의 은행이름과 계좌정보를 조회합니다.")
  @GetMapping("bank-details")
  public ResponseEntity<SellerResponse.BankDetails> getBankDetailsApi() {
    SellerResponse.BankDetails bankDetails = sellerService.getBankDetails();
    return ResponseEntity.status(HttpStatus.OK).body(bankDetails);
  }

}
