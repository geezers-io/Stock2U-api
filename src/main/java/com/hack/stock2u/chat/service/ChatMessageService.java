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
import com.hack.stock2u.file.repository.JpaAttachRepository;
import com.hack.stock2u.global.exception.GlobalException;
import com.hack.stock2u.global.service.SequenceGeneratorService;
import com.hack.stock2u.models.Attach;
import com.hack.stock2u.models.ChatMessage;
import com.hack.stock2u.models.Reservation;
import com.hack.stock2u.models.User;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
  private final JpaAttachRepository attachRepository;
  private final ReservationMessageHandler reservationMessageHandler;

  private final ChatAnalyzer chatAnalyzer = new ChatAnalyzer();
  // 메시지 저장

  public void saveAndSendMessage(SendChatMessage payload) {
    Reservation currentRoom = reservationRepository.findById(payload.roomId())
        .orElseThrow(GlobalException.NOT_FOUND::create);
    User u = sessionManager.getUserById(payload.userId());
    ChatMessageType type = chatAnalyzer.getMessageType(payload);
    Long oppositeUserId = getOppositeUserId(u.getId(), currentRoom);
    String profileImageUrl = getProfileImageUrl(u.getAvatarId());
    List<String> imageUrls = payload.imageIds()
        .stream()
        .map(this::getChatImageUrls)
        .toList();

    //일반 채팅 메세지 사용자들끼리 보낼때 사용
    String s = messageHandler.publishMessageSend(
        currentRoom, payload.message(), profileImageUrl, type, imageUrls, u
    );
    //메세지 저장
    saveMessage(currentRoom, u, payload.message(), payload.imageIds(), type);
    // 카운트와 메세지 알림 띄우기 위한 메세지
    chatPageMessageHandler.publishIdAndMessage(
        currentRoom,
        u,
        oppositeUserId,
        s,
        ChatAlertType.MESSAGE,
        type
    );
    reservationMessageHandler.publishMessage(
        payload.message(),
        u.getName(),
        oppositeUserId,
        type,
        ChatAlertType.MESSAGE
    );
  }

  private String getChatImageUrls(Long imageId) {
    Optional<Attach> byId = attachRepository.findById(imageId);
    String imageUrl = null;
    if (byId.isPresent()) {
      imageUrl = byId.get().getUploadPath();
    }
    return imageUrl;
  }

  private String getProfileImageUrl(Long avatarId) {
    Optional<Attach> byId = attachRepository.findById(avatarId);
    String profileImageUrl = null;
    if (byId.isPresent()) {
      profileImageUrl = byId.get().getUploadPath();
    }
    return profileImageUrl;
  }

  private ChatMessage saveMessage(
      Reservation reservation, User user, String message,
      List<Long> imageIds, ChatMessageType type) {

    return messageRepository.save(ChatMessage.builder()
        .id(sequenceGeneratorService.generateSequence(ChatMessage.SEQUENCE_NAME))
        .type(type)
        .roomId(reservation.getId())
        .username(user.getName())
        .message(message)
        .createdAt(new Date())
        .imageIds(imageIds)
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
    String profileImageUrl = getProfileImageUrl(seller.getAvatarId());
    //자동 메세지 발송
    String message = messageHandler.publishAutoMessageSend(
        reservation,
        AutoMessageTemplate.PURCHASE_REQUEST,
        ChatMessageType.AUTO,
        profileImageUrl,
        seller
    );
    //메세지 저장
    ChatMessage chatMessage = saveMessage(
        reservation,
        purchaser,
        message,
        null,
        ChatMessageType.AUTO
    );

    // 카운트와 메세지 알림 띄우기 위한 메세지
    chatPageMessageHandler.publishChatRoomCreationMessage(
        reservation,
        purchaser,
        sellerId,
        message,
        ChatAlertType.CREATION,
        ChatMessageType.AUTO,
        chatMessage
    );

  }

  public void saveAndSendAutoMessageApprove(ReservationApproveToMessage approveToMessage) {
    User user = sessionManager.getSessionUserByRdb();
    Long oppositeUserId = getOppositeUserId(user.getId(), approveToMessage.reservation());
    String profileImageUrl = getProfileImageUrl(user.getAvatarId());
    //자동 메세지 발송
    String s = messageHandler.publishAutoMessageSend(
        approveToMessage.reservation(),
        AutoMessageTemplate.SALE_APPROVED,
        ChatMessageType.AUTO,
        profileImageUrl,
        user
    );
    //메세지 저장
    saveMessage(approveToMessage.reservation(), user, s, null, ChatMessageType.AUTO);
    // 카운트와 메세지 알림 띄우기 위한 메세지
    chatPageMessageHandler.publishIdAndMessage(
        approveToMessage.reservation(),
        user,
        oppositeUserId,
        s,
        ChatAlertType.PROGRESS,
        ChatMessageType.AUTO);

  }

  public void saveAndSendAutoMessageCancel(Reservation reservation) {
    User user = sessionManager.getSessionUserByRdb();
    Long oppositeUserId = getOppositeUserId(user.getId(), reservation);
    String profileImageUrl = getProfileImageUrl(user.getAvatarId());
    //자동 메세지 발송
    String s = messageHandler.publishAutoMessageSend(
        reservation,
        AutoMessageTemplate.RESERVATION_CANCELLED,
        ChatMessageType.AUTO,
        profileImageUrl,
        user
    );
    //메세지 저장
    saveMessage(reservation, user, s, null, ChatMessageType.AUTO);
    // 카운트와 메세지 알림 띄우기 위한 메세지
    chatPageMessageHandler.publishIdAndMessage(
        reservation,
        user,
        oppositeUserId,
        s,
        ChatAlertType.CANCEL,
        ChatMessageType.AUTO
    );
  }
}
