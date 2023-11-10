package com.hack.stock2u.chat.service;

import com.hack.stock2u.chat.dto.response.AlertIdAndMessage;
import com.hack.stock2u.models.Reservation;
import com.hack.stock2u.models.User;
import com.hack.stock2u.utils.JsonSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatPageMessageHandler {
  private final SimpMessagingTemplate publisher;
  private final JsonSerializer jsonSerializer;

  public void publishIdAndMessage(Reservation reservation, User user, Long id, String message) {
    AlertIdAndMessage idAndMessage = AlertIdAndMessage.builder()
        .userName(user.getName())
        .userId(user.getId())
        .message(message)
        .reservationId(reservation.getId())
        .build();
    Object idAndMessageByJson = jsonSerializer.serialize(idAndMessage);
    publisher.convertAndSend("/topic/chat/alert/" + id, idAndMessageByJson);
  }
}
