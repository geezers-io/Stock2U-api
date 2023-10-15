package com.hack.stock2u.chat.service;

import com.hack.stock2u.chat.dto.request.ChatRequest;
import com.hack.stock2u.chat.dto.response.ChatResponse;
import com.hack.stock2u.models.ChatMessage;
import com.hack.stock2u.models.Reservation;
import com.hack.stock2u.user.repository.JpaChatRoomRepository;
import com.hack.stock2u.user.repository.MessageChatMongoRepository;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {

    private final MessageChatMongoRepository messageChatMongoRepository;
    private final JpaChatRoomRepository jpaChatRoomRepository;
    //채팅 내역 저장
    public ChatResponse save(ChatRequest chatRequest){
        //roomId 있는지 체크
        Reservation reservation =jpaChatRoomRepository.findById(chatRequest.id())
            .orElseThrow(()->new IllegalArgumentException("Invalid room id:"+ chatRequest.id()));
        //몽고디비에 채팅 메세지 생성
        ChatMessage chatMessage = ChatMessage.builder()
            .roomId(reservation.getId())
            .message(chatRequest.message())
            .timestamp(new Date())
            .build();
        //생성한 메세지 저장
        ChatMessage savedMessage = messageChatMongoRepository.save(chatMessage);
        return new ChatResponse(savedMessage.getRoomId(), savedMessage.getMessage(), savedMessage.getTimestamp());
    }
//    //방 이름으로 메세지 가져오기
//    public void getAllById(Long id,Long roomId)


}
