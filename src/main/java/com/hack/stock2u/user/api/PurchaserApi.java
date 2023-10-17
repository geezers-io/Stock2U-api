package com.hack.stock2u.user.api;

import com.hack.stock2u.user.dto.PurchaserRequest;
import com.hack.stock2u.user.service.PurchaserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "M. 마이페이지 - 구매자 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/my/purchaser")
public class PurchaserApi {
  private final PurchaserService userService;

  @Operation(summary = "구매자 회원정보 수정 API", description = "판매자 회원정보를 수정합니다. (only phone)")
  @PutMapping
  public ResponseEntity<Void> updateUser(
      @RequestBody @Valid PurchaserRequest.Update updateRequest
  ) {
    userService.update(updateRequest);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

}
