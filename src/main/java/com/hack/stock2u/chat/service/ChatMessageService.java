package com.hack.stock2u.chat.service;

import com.hack.stock2u.authentication.service.SessionManager;
import com.hack.stock2u.chat.dto.ReservationApproveToMessage;
import com.hack.stock2u.chat.dto.ReservationProductPurchaser;
import com.hack.stock2u.chat.dto.request.SendChatMessage;
import com.hack.stock2u.chat.repository.JpaReservationRepository;
import com.hack.stock2u.chat.repository.MessageChatMongoRepository;
import com.hack.stock2u.constant.AutoMessageTemplate;
import com.hack.stock2u.constant.ChatAlertType;
import com.hack.stock2u.constant.ChatMessageType;
import com.hack.stock2u.global.exception.GlobalException;
import com.hack.stock2u.models.ChatMessage;
import com.hack.stock2u.models.Reservation;
import com.hack.stock2u.models.User;
import com.hack.stock2u.product.repository.JpaProductRepository;
import com.hack.stock2u.user.repository.JpaUserRepository;
import com.hack.stock2u.utils.JsonSerializer;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {
  private final JpaReservationRepository reservationRepository;
  private final MessageChatMongoRepository messageRepository;
  private final JpaUserRepository userRepository;
  private final SimpMessagingTemplate messagingTemplate;
  private final JpaProductRepository productRepository;
  private final SessionManager sessionManager;
  private final JsonSerializer jsonSerializer;
  private final MessageHandler messageHandler;
  private final ChatPageMessageHandler chatPageMessageHandler;
  // 메시지 저장

  public void saveAndSendMessage(SendChatMessage request, Long roomId) {
    Reservation currentRoom = reservationRepository.findById(request.roomId())
        .orElseThrow(GlobalException.NOT_FOUND::create);
    User u = sessionManager.getSessionUserByRdb();
    ChatMessageType type = ChatMessageType.TEXT;
    Long opUserId = getOpUserId(u.getId(), currentRoom);
    if (request.message() == null) {
      type = ChatMessageType.IMAGE;
    }
    //일반 채팅 메세지 사용자들끼리 보낼때 사용
    String s = messageHandler.publishMessageSend(
        currentRoom, opUserId, request.message(), type, request.imageIds());
    //메세지 저장
    saveMessage(currentRoom, u, request.message(), request.imageIds(), type);
    // 카운트와 메세지 알림 띄우기 위한 메세지
    chatPageMessageHandler.publishIdAndMessage(
        currentRoom, u, opUserId, s, ChatAlertType.MESSAGE, type);

  }

  private ChatMessage saveMessage(
      Reservation reservation, User user, String message,
      List<Long> imageIds, ChatMessageType type) {

    return messageRepository.save(ChatMessage.builder()
        .type(type)
        .roomId(reservation.getId())
        .userName(user.getName())
        .message(message)
        .createdAt(new Date())
        .imageIds(imageIds)

        .build());
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
    User purchaser = rpp.purchaser();
    Long opUserId = getOpUserId(purchaser.getId(), reservation);
    Long sellerId = reservation.getSeller().getId();

    //자동 메세지 발송
    String message = messageHandler.publishAutoMessageSend(
        reservation,
        opUserId,
        AutoMessageTemplate.PURCHASE_REQUEST
    );
    //메세지 저장
    ChatMessage chatMessage = saveMessage(
        reservation,
        purchaser,
        message,
        null,
        ChatMessageType.TEXT
    );
    log.debug("chatMessage: {}", chatMessage);
    // 카운트와 메세지 알림 띄우기 위한 메세지
    chatPageMessageHandler.publishChatRoomCreationMessage(
        reservation,
        purchaser,
        sellerId,
        message,
        ChatAlertType.CREATION,
        ChatMessageType.TEXT,
        chatMessage
    );

  }

  public void saveAndSendAutoMessageApprove(ReservationApproveToMessage approveToMessage) {
    User u = sessionManager.getSessionUserByRdb();
    Long oppositeUserId = getOpUserId(u.getId(), approveToMessage.reservation());
    //자동 메세지 발송
    String s = messageHandler.publishAutoMessageSend(approveToMessage.reservation(), oppositeUserId,
        AutoMessageTemplate.SALE_APPROVED);
    //메세지 저장
    saveMessage(approveToMessage.reservation(), u, s, null, ChatMessageType.TEXT);
    // 카운트와 메세지 알림 띄우기 위한 메세지
    chatPageMessageHandler.publishIdAndMessage(approveToMessage.reservation(), u, oppositeUserId, s,
        ChatAlertType.PROGRESS, ChatMessageType.TEXT);

  }

  public void saveAndSendAutoMessageCancel(Reservation reservation) {
    User u = sessionManager.getSessionUserByRdb();
    Long oppositeUserId = getOpUserId(u.getId(), reservation);
    //자동 메세지 발송
    String s = messageHandler.publishAutoMessageSend(reservation, oppositeUserId,
        AutoMessageTemplate.RESERVATION_CANCELLED);
    //메세지 저장
    saveMessage(reservation, u, s, null, ChatMessageType.TEXT);
    // 카운트와 메세지 알림 띄우기 위한 메세지
    chatPageMessageHandler.publishIdAndMessage(reservation, u, oppositeUserId, s,
        ChatAlertType.CANCEL, ChatMessageType.TEXT);
  }
}
