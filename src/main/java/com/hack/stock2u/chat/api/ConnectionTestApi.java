package com.hack.stock2u.chat.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat-test")
public class ConnectionTestApi {

  @GetMapping
  public String serveChatConnectionPage() {
    return "chat";
  }

}
