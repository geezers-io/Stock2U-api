package com.hack.stock2u.chat.service;

import com.hack.stock2u.chat.dto.ChatMessageObjectForSerialize;
import com.hack.stock2u.constant.AutoMessageTemplate;
import com.hack.stock2u.constant.ChatMessageType;
import com.hack.stock2u.models.Reservation;
import com.hack.stock2u.utils.JsonSerializer;
import java.text.MessageFormat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageHandler {
  private final SimpMessagingTemplate publisher;
  private final JsonSerializer jsonSerializer;

  //자동메세지 전용
  public String publishAutoMessageSend(
      Reservation reservation, Long id, AutoMessageTemplate template) {
    String message = MessageFormat.format(
        template.getTemplate(), reservation.getProduct().getTitle());
    Object serialize = jsonSerializer.serialize(ChatMessageObjectForSerialize.builder()
        .username(reservation.getPurchaser().getName())
        .message(message)
        .createdAt(reservation.getBasicDate().getCreatedAt())
        .imageId(reservation.getPurchaser().getAvatarId())
        .build()
    );
    String destination = "/topic/chat/room/" + reservation.getId();
    publisher.convertAndSend(destination + id, serialize);
    return message;
  }

  //사용자들끼리 일반 채팅할때 사용
  public String publishMessageSend(
      Reservation reservation, Long id, String message,
      ChatMessageType type, List<String> imageUrls) {
    Object serialize = jsonSerializer.serialize(ChatMessageObjectForSerialize.builder()

        .username(reservation.getPurchaser().getName())
        .message(message)
        .createdAt(reservation.getBasicDate().getCreatedAt())
        .imageId(reservation.getPurchaser().getAvatarId())
        .build()
    );
    String destination = "/topic/chat/room/" + reservation.getId();
    publisher.convertAndSend(destination + id, serialize);
    return message;
  }
}
