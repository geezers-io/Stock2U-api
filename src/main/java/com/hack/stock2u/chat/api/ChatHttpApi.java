package com.hack.stock2u.chat.api;

import com.hack.stock2u.chat.dto.response.ChatHistories;
import com.hack.stock2u.chat.service.ChatHistoryLoader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservation/chats/history")
public class ChatHttpApi {

  private final ChatHistoryLoader historyLoader;

  @Operation(summary = "채팅 내역 불러오기 API", description = "커서 기반으로 불러옵니다")
  @GetMapping
  public ResponseEntity<ChatHistories> getChatHistoryApi(
      @Parameter(description = "예약(채팅) ID", required = true)
      @RequestParam("reservationId") Long reservationId,
      @Parameter(description = "예약(채팅) ID")
      @RequestParam(value = "cursor", required = false) Long cursor
  ) {
    ChatHistories histories = historyLoader.getHistories(reservationId, cursor);
    return ResponseEntity.ok(histories);
  }

}
