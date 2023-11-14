package com.hack.stock2u.user.api;

import com.hack.stock2u.file.dto.SimpleFile;
import com.hack.stock2u.product.service.PurchasedHistoryService;
import com.hack.stock2u.user.dto.PurchasedHistory;
import com.hack.stock2u.user.dto.PurchaserRequest;
import com.hack.stock2u.user.dto.SearchDate;
import com.hack.stock2u.user.service.MyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Date;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "M. 마이페이지 - 공용 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/my")
public class MyApi {
  private final MyService myService;
  private final PurchasedHistoryService historyService;

  @Operation(summary = "사용자 - 아바타 이미지 변경 API")
  @PostMapping(value = "/avatar", consumes = "multipart/form-data")
  public ResponseEntity<SimpleFile> changeAvatarImageApi(
      @Parameter(
          name = "file",
          description = "변경할 이미지 파일",
          required = true
      )
      @RequestPart("file")MultipartFile multipartFile
  ) {
    SimpleFile simpleFile = myService.uploadAvatarImage(multipartFile);
    return ResponseEntity.status(HttpStatus.CREATED).body(simpleFile);
  }

  @Operation(summary = "사용자 - 전화번호 변경 API", description = "사용자 전화번호를 변경합니다.")
  @PatchMapping("/phone")
  public ResponseEntity<Void> updateUser(
      @RequestBody @Valid PurchaserRequest.Update updateRequest
  ) {
    myService.update(updateRequest);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(summary = "구매자 구매기록 조회 API", description = "구매자의 재고 구매기록을 조회합니다")
  @GetMapping("/history")
  public ResponseEntity<Object> getPurchasedHistories(
      @Parameter(description = "필터 시작 날짜")
      @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy/MM/dd") Date startDate,
      @Parameter(description = "필터 끝 날짜")
      @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy/MM/dd") Date endDate,
      @Parameter(description = "조회할 페이지 넘버(0부터 시작)", required = true, example = "0")
      @RequestParam("page") int page,
      @Parameter(description = "가져올 데이터 갯수 단위", required = true, example = "10")
      @RequestParam("size") int size
  ) {
    PageRequest pageable = PageRequest.of(page, size);
    SearchDate params = new SearchDate(startDate, endDate);
    Page<PurchasedHistory> histories = historyService.getHistories(params, pageable);
    return ResponseEntity.ok(histories);
  }

}
