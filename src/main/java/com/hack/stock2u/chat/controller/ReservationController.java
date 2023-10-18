package com.hack.stock2u.chat.controller;

import com.hack.stock2u.chat.dto.request.ReservationRequestDto;
import com.hack.stock2u.chat.dto.response.ReservationResponse;
import com.hack.stock2u.chat.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  @Operation(summary = "예약 생성 API", description = "클라이언트가 구매 예약 요청을 보냈을때 예약을 위한 채팅방 생성")
  @PostMapping("/room")
  public ResponseEntity<ReservationResponse> createReservation(
      @RequestBody @Valid ReservationRequestDto.CreateReservationRequest createReservationRequest
  ) {
    reservationService.createRoom(createReservationRequest);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(summary = "예약 취소 API", description = "예약을 위한 채팅방을 삭제할때 사용하는 API")
  @DeleteMapping("/room/{id}")
  public ResponseEntity<Void> removeReservationApi(@PathVariable("id") Long id) {
    reservationService.remove(id);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
  //  @Operation(summary = "채팅방 조회 API", description = "특정 이용자의 채팅 내역 조회")
  //  @GetMapping("/rooms")
  //  public ResponseEntity<>
}
