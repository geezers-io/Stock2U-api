package com.hack.stock2u.chat.controller;

import com.hack.stock2u.chat.service.ChatMessageService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

  private final ChatMessageService messageService;

  //  public
  //채팅방 내용 가져오기 페이징 기법으로
}
