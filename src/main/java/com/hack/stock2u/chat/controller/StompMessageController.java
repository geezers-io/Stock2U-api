package com.hack.stock2u.chat.controller;


import com.hack.stock2u.chat.dto.request.SendChatMessage;
import com.hack.stock2u.chat.dto.response.ChatMessageResponse;
import com.hack.stock2u.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StompMessageController {

  private final ChatMessageService messageService;



  // 채팅방 대화
  @MessageMapping("/chat/talk/{roomId}")
  public void talkUser(@DestinationVariable("roomId") Long roomId, @Payload SendChatMessage request) {

    messageService.saveAndSendMessage(request, roomId);
  }
  //채팅방 내용 가져오기 페이징 기법으로


}



