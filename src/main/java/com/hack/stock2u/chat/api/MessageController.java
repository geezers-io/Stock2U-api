package com.hack.stock2u.chat.api;

import com.hack.stock2u.chat.service.ChatMessageService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

  private final ChatMessageService messageService;

}
