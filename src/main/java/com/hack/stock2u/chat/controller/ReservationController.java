package com.hack.stock2u.chat.controller;

import com.hack.stock2u.chat.dto.request.ReportRequest;
import com.hack.stock2u.chat.dto.request.ReservationApproveRequest;
import com.hack.stock2u.chat.dto.request.ReservationRequestDto;
import com.hack.stock2u.chat.service.ChatMessageService;
import com.hack.stock2u.chat.service.ReservationService;
import com.hack.stock2u.constant.ReservationStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "C. 채팅방 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/reservation")
public class ReservationController {
  private final ReservationService reservationService;
  //create에도 session 추가하기
  private final SimpMessagingTemplate messagingTemplate;
  private final ChatMessageService chatMessageService;

  @Operation(summary = "예약 생성 API", description = "클라이언트가 구매 예약 요청을 보냈을때 예약을"
      + " 위한 채팅방 생성 + 판매자에게 자동 메세지 발송")
  @PostMapping("/room")
  public ResponseEntity<Void> createReservation(
      @RequestBody @Valid ReservationRequestDto.CreateReservationRequest createReservationRequest
  ) {
    Long id = reservationService.createRoom(createReservationRequest);
    //예약 눌렀을때 메세지 보내지는 방식
    //상태 보내야함 근데 null로 갈꺼 같음 그래서 상태 하나 추가해야될듯?
    //메세지로 ReservationStatus를 넘기는게 맞을까? 구매자가 판매자에게 넘기는 형식임
    chatMessageService.saveAndSendAutoMessage(createReservationRequest, id);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
  //예약승인 api

  @Operation(summary = "예약 승인 API", description = "예약을 승인 할때 나오는 API + 구매자에게 자동 메세지 발송")
  @PostMapping("/approve")
  public ResponseEntity<ReservationStatus> approveReservationApi(
      ReservationApproveRequest request) {

    //메세지로 상태값 넘기는게 맞는지?
    //approve에서 상태값을 넘기는게 나을것 같은데 : 일단 넘김
    ReservationStatus approve = reservationService.approve(request);
    chatMessageService.saveAndSendAutoMessageApprove(request);
    return ResponseEntity.status(HttpStatus.OK).body(approve);
  }

  @Operation(summary = "예약 삭제 API", description = "예약을 위한 채팅방을 삭제할때 사용하는 API")
  @DeleteMapping("/room/{id}")
  public ResponseEntity<Void> removeReservationApi(@PathVariable("id") Long id) {
    //어떻게 id 줄지 생각해야함
    reservationService.remove(id);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(summary = "신고 API", description = "채팅방에서 사용자 신고 API")
  @PostMapping("/room/report")
  public ResponseEntity<Short> reportUserApi(@RequestBody @Valid ReportRequest request) {
    Short reportCount = reservationService.report(request);
    return ResponseEntity.status(HttpStatus.OK).body(reportCount);
  }
}
