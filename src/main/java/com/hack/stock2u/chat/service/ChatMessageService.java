package com.hack.stock2u.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hack.stock2u.authentication.dto.SessionUser;
import com.hack.stock2u.authentication.service.SessionManager;
import com.hack.stock2u.chat.dto.ReservationProductPurchaser;
import com.hack.stock2u.chat.dto.request.ReservationApproveRequest;
import com.hack.stock2u.chat.dto.request.SendChatMessage;
import com.hack.stock2u.chat.dto.response.AlertIdAndMessage;
import com.hack.stock2u.chat.dto.response.ReservationMessageResponse;
import com.hack.stock2u.chat.repository.JpaReservationRepository;
import com.hack.stock2u.chat.repository.MessageChatMongoRepository;
import com.hack.stock2u.global.exception.GlobalException;
import com.hack.stock2u.models.ChatMessage;
import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.Reservation;
import com.hack.stock2u.models.User;
import com.hack.stock2u.product.repository.JpaProductRepository;
import com.hack.stock2u.user.repository.JpaUserRepository;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
  private final JpaReservationRepository reservationRepository;
  private final MessageChatMongoRepository messageRepository;
  private final JpaUserRepository userRepository;
  private final SimpMessagingTemplate messagingTemplate;
  private final JpaProductRepository productRepository;
  private final SessionManager sessionManager;
  private final ObjectMapper objectMapper;
  // 메시지 저장

  public void saveAndSendMessage(SendChatMessage request, Long roomId) {
    Reservation currentRoom = reservationRepository.findById(request.roomId())
        .orElseThrow(GlobalException.NOT_FOUND::create);
    SessionUser s = sessionManager.getSessionUser();
    String username = s.username();

    ChatMessage message = messageRepository.save(ChatMessage.builder()
        .roomId(currentRoom.getId())
        .userName(username)
        .message(request.message())
        .createdAt(new Date())
        .build());
    String destination = "/topic/chat/room/" + roomId;
    Long opUserId = getOpUserId(s.id(), currentRoom);
    messagingTemplate.convertAndSend(destination, message.getMessage());
    //아바타 이미지 텏스트 시간 메세지 user
    AlertIdAndMessage idAndMessage = AlertIdAndMessage.builder()
        .message(message.getMessage())
        .reservationId(roomId)
        .build();

    try {
      String idAndMessageByJson = objectMapper.writeValueAsString(idAndMessage);
      messagingTemplate.convertAndSend("/topic/chat/alert/" + opUserId, idAndMessageByJson);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

  }

  private Long getOpUserId(Long id, Reservation reservation) {
    Long opUserId = null;

    User seller = reservation.getSeller();
    User purchaser = reservation.getPurchaser();
    if (seller != null && purchaser != null) {
      opUserId = (seller.getId().equals(id) ? purchaser.getId() : seller.getId());
    }
    return opUserId;
  }

  public void saveAndSendAutoMessage(ReservationProductPurchaser rpp) {
    Reservation reservation = rpp.reservation();
    Product product = rpp.product();
    User purchaser = rpp.purchaser();

    ChatMessage message = messageRepository.save(
        ChatMessage.builder()
          .roomId(reservation.getId())
          .userName(purchaser.getName())
          .message("[자동 발신 메세지] \n" + product.getName() + "구매를 원합니다.")
          .createdAt(new Date())
          .build()
    );
    // 밑에 dto 지워 그리고 message 가공하던가 하셈
    ReservationMessageResponse messageResponse = ReservationMessageResponse
        .makingReservationMessage(reservation, product, purchaser, message);
    String destination = "/topic/chat/room/" + reservation.getId();

    messagingTemplate.convertAndSend(destination, messageResponse);
  }

  public void saveAndSendAutoMessageApprove(ReservationApproveRequest request) {
    Long u = sessionManager.getSessionUser().id();
    User seller = userRepository.findById(u)
        .orElseThrow(GlobalException.NOT_FOUND::create);
    ChatMessage message = messageRepository.save(ChatMessage.builder()
        .roomId(request.roomId())
        .userName(seller.getName())
        .message("[자동 발신 메세지] \n 판매가 예약되었습니다.")
        .createdAt(new Date())
        .build()
    );
    String destination = "/topic/chat/room/" + request.roomId();
    messagingTemplate.convertAndSend(destination, message);
  }
}

