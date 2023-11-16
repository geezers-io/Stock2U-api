package com.hack.stock2u.chat.service;

import com.hack.stock2u.chat.dto.request.SendChatMessage;
import com.hack.stock2u.constant.ChatMessageType;
import java.util.List;

public class ChatAnalyzer {
  public ChatMessageType getMessageType(SendChatMessage payload) {
    List<Long> imageIds = payload.imageIds();
    if (imageIds != null) {
      return ChatMessageType.IMAGE;
    }
    return ChatMessageType.TEXT;
  }
}
