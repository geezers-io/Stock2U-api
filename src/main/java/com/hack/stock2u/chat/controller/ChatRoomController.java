package com.hack.stock2u.chat.controller;

import com.hack.stock2u.chat.dto.request.ChatRoomRequestDto;
import com.hack.stock2u.chat.dto.request.ReservationRequest;
import com.hack.stock2u.chat.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "채팅방 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomController {
  private final ChatRoomService chatRoomService;

  @Operation(summary = "채팅방 생성 API", description = "클라이언트가 구매예약 요청을 보냈을때 채팅방 생성")
  @PostMapping("/room")
  public ResponseEntity<ReservationRequest> createReservation(
      @RequestBody @Valid ChatRoomRequestDto.CreateReservationRequest createReservationRequest
  ) {
    ReservationRequest reservationRequest = chatRoomService.createRoom(createReservationRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(reservationRequest);
  }

  //  @Operation(summary = "채팅방 조회 API", description = "특정 이용자의 채팅 내역 조회")
  //  @GetMapping("/rooms")
  //  public ResponseEntity<>
}
