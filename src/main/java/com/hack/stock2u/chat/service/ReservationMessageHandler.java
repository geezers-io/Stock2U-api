package com.hack.stock2u.chat.service;

import com.hack.stock2u.constant.MessageTemplate;
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

}
