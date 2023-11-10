package com.hack.stock2u.chat.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hack.stock2u.chat.dto.ReservationApproveToMessage;
import com.hack.stock2u.chat.dto.ReservationProductPurchaser;
import com.hack.stock2u.chat.dto.request.ChangeStatusRequest;
import com.hack.stock2u.chat.dto.request.ReportRequest;
import com.hack.stock2u.chat.dto.request.ReservationApproveRequest;
import com.hack.stock2u.chat.dto.response.PurchaserSellerReservationsResponse;
import com.hack.stock2u.chat.dto.response.SimpleReservation;
import com.hack.stock2u.chat.service.ChatMessageService;
import com.hack.stock2u.chat.service.ReservationService;
import com.hack.stock2u.constant.ReservationStatus;
import com.hack.stock2u.constant.UserRole;
import com.hack.stock2u.global.dto.GlobalResponse;
import com.hack.stock2u.models.Reservation;
import com.hack.stock2u.utils.RoleGuard;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "C. 예약 API (채팅, 재고 관련)")
@RequiredArgsConstructor
@RestController
@RequestMapping("/reservation")
public class ReservationApi {
  private final ReservationService reservationService;

  private final ChatMessageService chatMessageService;

  @RoleGuard(roles = UserRole.PURCHASER)
  @Operation(summary = "예약 생성 API", description = "클라이언트가 구매 예약 요청을 보냈을때 예약을"
      + " 위한 채팅방 생성 + 판매자에게 자동 메세지 발송")
  @PostMapping("/{productId}")
  public ResponseEntity<GlobalResponse.Id> createReservation(
      @PathVariable("productId") Long productId
  ) {
    ReservationProductPurchaser ret = reservationService.create(productId);

    chatMessageService.saveAndSendAutoMessage(ret);
    return ResponseEntity.status(HttpStatus.CREATED).body(new GlobalResponse.Id(
        ret.reservation().getId()));
  }

  @Operation(summary = "예약 승인 API", description = "예약을 승인 할때 나오는 API + 구매자에게 자동 메세지 발송")
  @PatchMapping("/approve")
  public ResponseEntity<ReservationStatus> approveReservationApi(
      ReservationApproveRequest request) {

    ReservationApproveToMessage approveToMessage = reservationService.approve(request);
    chatMessageService.saveAndSendAutoMessageApprove(approveToMessage);
    return ResponseEntity.status(HttpStatus.OK).body(approveToMessage.status());
  }

  @Operation(summary = "예약 취소 API", description = "희정님이 상세페이지에서 예약 삭제할때 쓰는 API")
  @DeleteMapping("/{reservationId}")
  public ResponseEntity<Void> cancelReservationApi(
      @PathVariable("reservationId") Long reservationId
  ) {
    Reservation cancel = reservationService.cancel(reservationId);
    chatMessageService.saveAndSendAutoMessageCancel(cancel);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  //얼마 이상 누적되면 이용정지 로직 추가
  @Operation(summary = "신고 API", description = "채팅방에서 사용자 신고 API")
  @PostMapping("/report")
  public ResponseEntity<Short> reportUserApi(@RequestBody @Valid ReportRequest request) {
    Short reportCount = reservationService.report(request);
    return ResponseEntity.status(HttpStatus.OK).body(reportCount);
  }

  @Operation(summary = "채팅방 조회 API, 채팅방 검색 조회 API",
      description = "사용자가 가진 채팅방 + 검색으로 조회한 채팅방")
  @GetMapping("/chats")
  public ResponseEntity<Page<PurchaserSellerReservationsResponse>> getReservations(

      @Parameter(description = "게시글 제목으로 검색")
      @RequestParam(value = "title", required = false) String title,
      @Parameter(description = "조회할 페이지 넘버(0부터 시작)", required = true)
      @RequestParam("page") int page,
      @Parameter(description = "가져올 데이터 갯수 단위", required = true)
      @RequestParam("size") int size
  ) {
    PageRequest pageable = PageRequest.of(page, size);
    Page<PurchaserSellerReservationsResponse> purchaserReservations =
        reservationService.getReservations(pageable, title);
    return ResponseEntity.status(HttpStatus.OK).body(purchaserReservations);
  }

  //공통으로 해도 될꺼같은데? 상태값 변경만 추가해서 적용하면 되는거 아닌가?
  @Operation(summary = "공통 예약 내역 API", description = "공통으로 예약이 진행중인 내역을 보내주는 API")
  @GetMapping("/reservations")
  public ResponseEntity<Page<SimpleReservation>> getReservationsByDate(
      @Parameter(description = "필터 시작 날짜")
      @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy/MM/dd") Date startDate,
      @Parameter(description = "필터 끝 날짜")
      @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy/MM/dd") Date endDate,
      @Parameter(description = "조회할 페이지 넘버(0부터 시작)", required = true)
      @RequestParam("page") int page,
      @Parameter(description = "가져올 데이터 갯수 단위", required = true)
      @RequestParam("size") int size
  ) {
    PageRequest pageable = PageRequest.of(page, size);
    Page<SimpleReservation> reservations =
        reservationService.getReserveByDate(pageable, startDate, endDate);
    return ResponseEntity.status(HttpStatus.OK).body(reservations);
  }

  @RoleGuard(roles = UserRole.SELLER)
  @Operation(summary = "예약 상태 변경 API", description = "판매자 예약내역에서 상태를 변경할 수 있는 API")
  @PatchMapping("/status")
  public ResponseEntity<ReservationStatus> changeReservationStatus(
      @RequestBody @Valid ChangeStatusRequest request
  ) {
    return ResponseEntity.status(HttpStatus.OK).body(reservationService.changeStatus(request));
  }
}
