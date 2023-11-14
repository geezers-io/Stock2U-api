package com.hack.stock2u.chat.service;

import com.hack.stock2u.chat.dto.MessageAlert;
import com.hack.stock2u.constant.ChatAlertType;
import com.hack.stock2u.constant.ChatMessageType;
import com.hack.stock2u.constant.MessageTemplate;
import com.hack.stock2u.utils.JsonSerializer;
import java.text.MessageFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationMessageHandler {
  private final SimpMessagingTemplate publisher;
  private final JsonSerializer jsonSerializer;
  /**
   * 예약 생성에 대한 메시지를 발송합니다.

   * @param pid 구매자 ID
   * @param sid 판매자 ID
   */
  public void publishReservation(MessageTemplate template, String title, Long pid, Long sid) {
    String message = MessageFormat.format(template.getMessage(), title);
    String dest = "/topic/alert/";
    publisher.convertAndSend(dest + pid, message);
    publisher.convertAndSend(dest + sid, message);
  }

  public void publishMessage(
      String message,
      String userName,
      Long id,
      ChatMessageType type,
      ChatAlertType status) {
    Object serialize = jsonSerializer.serialize(
        MessageAlert.builder()
            .type(type)
            .message(message)
            .userName(userName)
            .status(status)
            .build()
    );
    String dest = "/topic/alert/";
    publisher.convertAndSend(dest + id, serialize);
  }

}
