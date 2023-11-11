package com.hack.stock2u.chat.service;

import com.hack.stock2u.chat.dto.response.AlertIdAndMessage;
import com.hack.stock2u.chat.dto.response.ChatMessageResponse;
import com.hack.stock2u.chat.dto.response.ChatRoomCreation;
import com.hack.stock2u.chat.dto.response.ChatRoomSummary;
import com.hack.stock2u.chat.dto.response.SimpleReservation;
import com.hack.stock2u.chat.dto.response.SimpleThumbnailImage;
import com.hack.stock2u.constant.ChatAlertType;
import com.hack.stock2u.constant.ChatMessageType;
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
      Reservation reservation,
      User user,
      Long id,
      String message,
      ChatAlertType status,
      ChatMessageType type
  ) {
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

  public void publishChatRoomCreationMessage(
      Reservation reservation,
      User purchaser,
      Long sellerId,
      String message,
      ChatAlertType type,
      ChatMessageType messageType,
      ChatMessage chatMessage
  ) {
    ChatMessageResponse latestChat = ChatMessageResponse.create(chatMessage);
    long count = reservationService.getCountOfMessage(purchaser.getName(), reservation.getId());
    SimpleThumbnailImage thumbnailImage = reservationService.getThumbnailImage(reservation);
    SimpleReservation reservationSummary = SimpleReservation.create(reservation, thumbnailImage);
    ChatRoomSummary chatRoomSummary = new ChatRoomSummary(latestChat, reservationSummary, count);

    ChatRoomCreation creation = ChatRoomCreation.builder()
        .chatRoomSummary(chatRoomSummary)
        .type(type)
        .chatMessageType(messageType)
        .build();

    Object object = jsonSerializer.serialize(creation);
    log.debug("purchaserId: {}, sellerId: {}", purchaser.getId(), sellerId);
    publisher.convertAndSend("/topic/chat/alert/" + purchaser.getId(), object);
    publisher.convertAndSend("/topic/chat/alert/" + sellerId, object);
  }

}