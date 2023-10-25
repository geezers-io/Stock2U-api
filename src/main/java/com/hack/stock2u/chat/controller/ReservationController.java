package com.hack.stock2u.chat.controller;

import com.hack.stock2u.chat.dto.ReservationApproveDto;

import com.hack.stock2u.chat.dto.request.ReservationRequestDto;
import com.hack.stock2u.chat.dto.response.ReservationResponse;
import com.hack.stock2u.chat.service.ChatMessageService;
import com.hack.stock2u.chat.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  @Operation(summary = "예약 생성 API", description = "클라이언트가 구매 예약 요청을 보냈을때 예약을 위한 채팅방 생성")
  @PostMapping("/room")
  public ResponseEntity<Void> createReservation(
      @RequestBody @Valid ReservationRequestDto.CreateReservationRequest createReservationRequest
  ) {
    reservationService.createRoom(createReservationRequest);
    //예약 눌렀을때 메세지 보내지는 방식
    //상태 보내야함
    chatMessageService.saveAndSendAutoMessage(createReservationRequest);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
  //예약승인 api

  @Operation(summary = "예약 승인 API", description = "예약을 승인 할때 나오는 API + 구매자에게 자동 메세지 발송")
  @PostMapping("/approve")
  public ResponseEntity<Void> approveReservationApi(ReservationApproveDto approveDto) {
  //채팅 발송하기 구현해야함
    reservationService.approve(approveDto);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(summary = "예약 삭제 API", description = "예약을 위한 채팅방을 삭제할때 사용하는 API")
  @DeleteMapping("/room/{id}")
  public ResponseEntity<Void> removeReservationApi(@PathVariable("id") Long id) {
    //어떻게 id 줄지 생각해야함
    reservationService.remove(id);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
//검색어로 조회

//  @Operation(summary = "예약 검색어 조회 API", description = "검색어로 예약을 검색할때 사용하는 API")
//  @GetMapping("/room")
//  //잔여 재고 이름으로
//  public ResponseEntity<List<ReservationResponse>> searchReservationApi(@RequestParam String productName) {
//    reservationService.search(productName);
//
//    return null;
//  }
//  @Operation(summary = "채팅방 조회 API", description = "특정 이용자의 채팅 내역 조회")
//  @GetMapping("/room")
//  public ResponseEntity<ReservationResponseDto.GetReservation> findReservationApi() {
//    ReservationResponseDto.GetReservation myReservation = reservationService.findMyReservation();
//    return ResponseEntity.status(HttpStatus.OK).body(myReservation);
//  }
}
