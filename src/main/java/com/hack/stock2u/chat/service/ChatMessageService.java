package com.hack.stock2u.chat.service;

import com.hack.stock2u.chat.dto.request.ChatMessageRequest;
import com.hack.stock2u.chat.repository.JpaChatRoomRepository;
import com.hack.stock2u.chat.repository.MessageChatMongoRepository;
import com.hack.stock2u.models.ChatMessage;
import com.hack.stock2u.models.Reservation;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
  private final MessageChatMongoRepository messageChatMongoRepository;
  private final JpaChatRoomRepository jpaChatRoomRepository;


  /**
   *메세지 내역 저장
   */

  @Transactional
  public Long save(ChatMessageRequest chatMessageRequest) {
    //roomId 있는지 체크
    Reservation reservation = jpaChatRoomRepository.findById(chatMessageRequest.roomId())
        .orElseThrow(() -> new IllegalArgumentException("채팅방 없어서 저장못해:"
            + chatMessageRequest.roomId()));

    //몽고디미 메세지 생성
    ChatMessage chatMessage = ChatMessage.builder()
        .roomId(reservation.getId())
        .userName(chatMessageRequest.name())
        .message(chatMessageRequest.message())
        .timeStamp(new Date())
        .build();
    //생성한 메세지 저장
    return this.messageChatMongoRepository.save(chatMessage).getRoomId();
  }
  //방 이름으로 메세지 가져오기

  //메세지 내역
  @Transactional
  public Page<ChatMessage> getChatMessagesByRoomId(Long roomId, Pageable pageable) {
    return this.messageChatMongoRepository.findByRoomId(roomId, pageable);
  }
}
