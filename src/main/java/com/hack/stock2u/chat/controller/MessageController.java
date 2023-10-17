package com.hack.stock2u.chat.controller;

import com.hack.stock2u.models.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {
  private final SimpMessageSendingOperations simpMessageSendingOperations;

  @MessageMapping("/chat/message")
  public void message(ChatMessage chatMessage) {
    simpMessageSendingOperations.convertAndSend("/queue/chat/room"
        + chatMessage.getId(), chatMessage);
  }
}
