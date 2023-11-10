package com.hack.stock2u.chat.service;

import com.hack.stock2u.chat.dto.response.AlertIdAndMessage;
import com.hack.stock2u.chat.dto.response.ChatMessageResponse;
import com.hack.stock2u.chat.dto.response.Creation;
import com.hack.stock2u.chat.dto.response.PurchaserSellerReservationsResponse;
import com.hack.stock2u.chat.dto.response.SimpleReservation;
import com.hack.stock2u.chat.dto.response.SimpleThumbnailImage;
import com.hack.stock2u.constant.ChatMessageType;
import com.hack.stock2u.constant.ReservationStatusForChatList;
import com.hack.stock2u.models.ChatMessage;
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
  private final ReservationService reservationService;

  public void publishIdAndMessage(
      Reservation reservation, User user, Long id, String message,
      ReservationStatusForChatList status, ChatMessageType type) {
    AlertIdAndMessage idAndMessage = AlertIdAndMessage.builder()
        .type(status)
        .userName(user.getName())
        .userId(user.getId())
        .message(message)
        .reservationId(reservation.getId())
        .chatMessageType(type)
        .build();
    Object idAndMessageByJson = jsonSerializer.serialize(idAndMessage);
    publisher.convertAndSend("/topic/chat/alert/" + id, idAndMessageByJson);
  }

  public void publishIdAndMessageIfCreation(
      Reservation reservation, User user, Long id, String message,
      ReservationStatusForChatList status, ChatMessageType type, ChatMessage chatMessage) {
    ChatMessageResponse messageResponse = ChatMessageResponse.create(chatMessage);
    long count = reservationService.getCountOfMessage(user.getName(), reservation.getId());
    SimpleThumbnailImage thumbnailImage = reservationService.getThumbnailImage(reservation);
    SimpleReservation simpleReservation = SimpleReservation.create(reservation, thumbnailImage);
    Creation build = Creation.builder()
        .simpleReservation(simpleReservation)
        .chatMessageResponse(messageResponse)
        .count(count)
        .status(status)
        .type(type)
        .build();
    Object object = jsonSerializer.serialize(build);
    publisher.convertAndSend("/topic/chat/alert/" + id, object);
  }

}
