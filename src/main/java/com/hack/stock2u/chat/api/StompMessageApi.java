package com.hack.stock2u.chat.api;


import com.hack.stock2u.chat.dto.request.SendChatMessage;
import com.hack.stock2u.chat.service.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompMessageApi {

  private final ChatMessageService messageService;
  private final SimpMessagingTemplate template;

  // 채팅방 대화
  @Operation(summary = "메세지 전송 + 저장", description = "메세지를 전송할시 저장 후, "
      + "sub를 하고 있는 사용자에게 메세지 전송")
  @MessageMapping("/chat/{roomId}")
  public void sendChatMessage(@Payload SendChatMessage request) {
    messageService.saveAndSendMessage(request);
  }

}
