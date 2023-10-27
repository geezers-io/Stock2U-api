package com.hack.stock2u.chat.service;

import com.hack.stock2u.chat.dto.ReservationProductPurchaser;
import com.hack.stock2u.chat.dto.request.ReservationApproveRequest;
import com.hack.stock2u.chat.dto.request.ReservationRequestDto;
import com.hack.stock2u.chat.dto.request.SendChatMessage;
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
  // 메시지 저장

  public void saveAndSendMessage(SendChatMessage request, Long roomId) {
    Reservation currentRoom = reservationRepository.findById(request.roomId())
        .orElseThrow(GlobalException.NOT_FOUND::create);
    User user = userRepository.findById(request.userId())
        .orElseThrow(GlobalException.NOT_FOUND::create);

    ChatMessage message = messageRepository.save(ChatMessage.builder()
        .roomId(currentRoom.getId())
        .userName(user.getName())
        .message(request.message())
        .createdAt(new Date())
        .build());
    String destination = "/topic/chat/room/" + roomId;
    messagingTemplate.convertAndSend(destination, message);
  }

  public void saveAndSendAutoMessage(ReservationProductPurchaser rpp) {
    Reservation reservation = rpp.reservation();
    Product product = rpp.product();
    User purchaser = rpp.purchaser();

    ChatMessage message = messageRepository.save(ChatMessage.builder()
        .roomId(reservation.getId())
        .userName(purchaser.getName())
        .message("[자동 발신 메세지] \n" + product.getName() + "구매를 원합니다.")
        .createdAt(new Date())
        .build());

    ReservationMessageResponse messageResponse = ReservationMessageResponse
        .makingReservationMessage(reservation, product, purchaser, message);
    String destination = "/topic/chat/room/" + reservation.getId();

    messagingTemplate.convertAndSend(destination, messageResponse);
  }

  public void saveAndSendAutoMessageApprove(ReservationApproveRequest request) {
    User seller = userRepository.findById(request.sellerId())
        .orElseThrow(GlobalException.NOT_FOUND::create);
    ChatMessage message = messageRepository.save(ChatMessage.builder()
        .roomId(request.roomId())
        .userName(seller.getName())
        .message("[자동 발신 메세지] \n 판매가 예약되었습니다")
        .createdAt(new Date())
        .build()
    );
    String destination = "/topic/chat/room/" + request.roomId();
    messagingTemplate.convertAndSend(destination, message);
  }
}

