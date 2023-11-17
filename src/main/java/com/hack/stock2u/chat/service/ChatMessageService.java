package com.hack.stock2u.chat.service;

import com.hack.stock2u.authentication.service.SessionManager;
import com.hack.stock2u.chat.dto.MessageCreation;
import com.hack.stock2u.chat.dto.ReservationApproveToMessage;
import com.hack.stock2u.chat.dto.ReservationProductPurchaser;
import com.hack.stock2u.chat.dto.request.SendChatMessage;
import com.hack.stock2u.chat.repository.JpaReservationRepository;
import com.hack.stock2u.chat.repository.MessageChatMongoRepository;
import com.hack.stock2u.constant.AutoMessageTemplate;
import com.hack.stock2u.constant.ChatAlertType;
import com.hack.stock2u.constant.ChatMessageType;
import com.hack.stock2u.constant.MessageTemplate;
import com.hack.stock2u.file.service.ImageProvider;
import com.hack.stock2u.global.exception.GlobalException;
import com.hack.stock2u.global.service.SequenceGeneratorService;
import com.hack.stock2u.models.ChatMessage;
import com.hack.stock2u.models.Reservation;
import com.hack.stock2u.models.User;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {
  private final JpaReservationRepository reservationRepository;
  private final MessageChatMongoRepository messageRepository;
  private final SessionManager sessionManager;
  private final MessageHandler messageHandler;
  private final SequenceGeneratorService sequenceGeneratorService;
  private final ChatPageMessageHandler chatPageMessageHandler;
  private final ImageProvider imageProvider;
  private final ReservationMessageHandler reservationMessageHandler;

  private final ChatAnalyzer chatAnalyzer = new ChatAnalyzer();
  // 메시지 저장

  public void saveAndSendMessage(SendChatMessage payload) {
    Reservation currentRoom = reservationRepository.findById(payload.roomId())
        .orElseThrow(GlobalException.NOT_FOUND::create);
    User u = sessionManager.getUserById(payload.userId());
    ChatMessageType type = chatAnalyzer.getMessageType(payload);
    Long oppositeUserId = getOppositeUserId(u.getId(), currentRoom);
    String profileImageUrl = imageProvider.getImageOrElseThrow(u.getAvatarId());

    List<String> imageUrls = imageProvider.getImageUrls(payload.imageIds());

    //일반 채팅 메세지 사용자 들끼리 보낼때 사용
    String message = messageHandler.publishMessageSend(
        currentRoom, payload.message(), profileImageUrl, type, imageUrls, u
    );

    MessageCreation messageDetails = MessageCreation.builder()
        .user(u)
        .profileImageUrl(profileImageUrl)
        .imageIds(payload.imageIds())
        .message(message)
        .build();

    //메세지 저장
    saveMessage(currentRoom, messageDetails, type);

    // 카운트와 메세지 알림 띄우기 위한 메세지
    chatPageMessageHandler.publishIdAndMessage(
        currentRoom,
        u,
        oppositeUserId,
        message,
        ChatAlertType.MESSAGE,
        type
    );
    reservationMessageHandler.publishMessage(
        MessageTemplate.ALERT_MESSAGE,
        payload.message(),
        u.getName(),
        oppositeUserId,
        type
    );
  }

  private ChatMessage saveMessage(
      Reservation reservation,
      MessageCreation messageDetails,
      ChatMessageType type
  ) {
    return messageRepository.save(ChatMessage.builder()
        .id(sequenceGeneratorService.generateSequence(ChatMessage.SEQUENCE_NAME))
        .type(type)
        .roomId(reservation.getId())
        .username(messageDetails.user().getName())
        .message(messageDetails.message())
        .createdAt(new Date())
        .imageIds(messageDetails.imageIds())
        .build());
  }

  private Long getOppositeUserId(Long id, Reservation reservation) {
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
    User seller = reservation.getSeller();
    Long sellerId = seller.getId();
    String profileImageUrl = imageProvider.getImageOrElseThrow(seller.getAvatarId());

    //자동 메세지 발송
    String message = messageHandler.publishAutoMessageSend(
        reservation,
        AutoMessageTemplate.PURCHASE_REQUEST,
        ChatMessageType.AUTO,
        profileImageUrl,
        seller
    );

    // 메시지 객체 작성
    MessageCreation messageDetails = MessageCreation.builder()
        .user(purchaser)
        .profileImageUrl(profileImageUrl)
        .message(message)
        .build();

    //메세지 저장
    ChatMessage chatMessage = saveMessage(reservation, messageDetails, ChatMessageType.AUTO);

    // 카운트와 메세지 알림 띄우기 위한 메세지
    chatPageMessageHandler.publishChatRoomCreationMessage(
        reservation,
        purchaser,
        sellerId,
        ChatAlertType.CREATION,
        ChatMessageType.AUTO,
        chatMessage
    );

  }

  public void saveAndSendAutoMessageApprove(ReservationApproveToMessage approveToMessage) {
    User user = sessionManager.getSessionUserByRdb();
    Long oppositeUserId = getOppositeUserId(user.getId(), approveToMessage.reservation());
    String profileImageUrl = imageProvider.getImageOrElseThrow(user.getAvatarId());

    //자동 메세지 발송
    String message = messageHandler.publishAutoMessageSend(
        approveToMessage.reservation(),
        AutoMessageTemplate.SALE_APPROVED,
        ChatMessageType.AUTO,
        profileImageUrl,
        user
    );

    // 메세지 저장 객체 작성
    MessageCreation messageDetails = MessageCreation.builder()
        .user(user)
        .message(message)
        .profileImageUrl(profileImageUrl)
        .build();

    //메세지 저장
    saveMessage(approveToMessage.reservation(), messageDetails, ChatMessageType.AUTO);

    // 카운트와 메세지 알림 띄우기 위한 메세지
    chatPageMessageHandler.publishIdAndMessage(
        approveToMessage.reservation(),
        user,
        oppositeUserId,
        message,
        ChatAlertType.PROGRESS,
        ChatMessageType.AUTO);

  }

  public void saveAndSendAutoMessageCancel(Reservation reservation) {
    User user = sessionManager.getSessionUserByRdb();
    Long oppositeUserId = getOppositeUserId(user.getId(), reservation);
    String profileImageUrl = imageProvider.getImageOrElseThrow(user.getAvatarId());

    //자동 메세지 발송
    String message = messageHandler.publishAutoMessageSend(
        reservation,
        AutoMessageTemplate.RESERVATION_CANCELLED,
        ChatMessageType.AUTO,
        profileImageUrl,
        user
    );

    //메시지 저장 객체 생성
    MessageCreation messageDetails = MessageCreation.builder()
        .user(user)
        .profileImageUrl(profileImageUrl)
        .message(message)
        .imageIds(null)
        .build();

    //메세지 저장
    saveMessage(reservation, messageDetails, ChatMessageType.AUTO);

    // 카운트와 메세지 알림 띄우기 위한 메세지
    chatPageMessageHandler.publishIdAndMessage(
        reservation,
        user,
        oppositeUserId,
        message,
        ChatAlertType.CANCEL,
        ChatMessageType.AUTO
    );
  }
}
