package com.hack.stock2u.chat.service;

import com.hack.stock2u.chat.dto.request.ReservationRequestDto;
import com.hack.stock2u.chat.dto.request.SendChatMessage;
import com.hack.stock2u.chat.dto.response.ChatMessageResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
//    Reservation currentRoom = reservationRepository.findById(request.roomId())
//        .orElseThrow(IllegalArgumentException::new);
//    User user = userRepository.findById(request.userId())
//        .orElseThrow(() -> new IllegalArgumentException("사람 없는데?"));
    //request말고 request로 불러운 애들에서 찾으셈
    ChatMessage message = messageRepository.save(ChatMessage.builder()
        .roomId(request.roomId())
        .userName(request.sender())
        .message(request.message())
        .createdAt(new Date())
        .build());
    String destination = "/topic/chat/room/" + roomId;
    messagingTemplate.convertAndSend(destination, message);
  }


  public void saveAndSendAutoMessage(ReservationRequestDto.CreateReservationRequest createReservationRequest) {
    Reservation reservation = reservationRepository.findById(createReservationRequest.productId())
        .orElseThrow(GlobalException.NOT_FOUND::create);
    User user = userRepository.findById(createReservationRequest.purchaserId())
        .orElseThrow(GlobalException.NOT_FOUND::create);
    Product product = productRepository.findById(createReservationRequest.productId())
        .orElseThrow(GlobalException.NOT_FOUND::create);
    ChatMessage message = messageRepository.save(ChatMessage.builder()
        .roomId(reservation.getId())
        .userName(user.getName())
        .message("[자동 발신 메세지] \n" + product.getName() + "구매를 원합니다.")
        .createdAt(new Date())
        .build());
    String destination = "/topic/chat/room/" + reservation.getId();
    messagingTemplate.convertAndSend(destination, message);
  }
}

//ChatMessageResponse 이거는 모든 대화 내용 가져올때 쓰는걸로

//@Service
//@RequiredArgsConstructor
//public class ChatMessageService {
//  private final MessageChatMongoRepository messageChatMongoRepository;
//  private final JpaReservationRepository reservationRepository;
//  private final JpaUserRepository userRepository;
//
//  /**
//   *메세지 내역 저장
//   */
//  //나한테 다시 메시지 전달해야되니까 return 값 chatMessageResponse여야하지 않음?
//  @Transactional
//  public ChatMessageResponse save(ChatMessageRequest chatMessageRequest) {
//    //roomId 있는지 체크
//    Reservation reservation = reservationRepository.findById(chatMessageRequest.roomId())
//        .orElseThrow(() -> new IllegalArgumentException("채팅방 없어서 저장못해:"
//            + chatMessageRequest.roomId()));
//    User user = userRepository.findById(chatMessageRequest.userId())
//        .orElseThrow(() -> new IllegalArgumentException("사람 없는데?"));
//    //몽고디미 메세지 생성
//
//    ChatMessage chatMessage = messageChatMongoRepository.save(ChatMessage.builder()
//        .userId(user.getId())
//        .roomId(reservation.getId())
//        .userName(user.getName())
//        .message(chatMessageRequest.message())
//        .createdAt(new Date())
//        .type(chatMessageRequest.type())
//        .build());
//    //생성한 메세지 저장
//    return ChatMessageResponse.response(chatMessage);
//  }
//
//
//  //방 이름으로 메세지 가져오기
//
//  //메세지 내역
//  @Transactional
//  public Page<ChatMessage> getChatMessagesByRoomId(Long roomId, Pageable pageable) {
//    return this.messageChatMongoRepository.findByRoomId(roomId, pageable);
//  }
//}
