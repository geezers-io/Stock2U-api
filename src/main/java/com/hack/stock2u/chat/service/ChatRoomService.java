package com.hack.stock2u.chat.service;


import com.hack.stock2u.chat.dto.request.ChatRoomRequestDto;
import com.hack.stock2u.chat.dto.request.ReservationRequest;
import com.hack.stock2u.chat.repository.JpaChatRoomRepository;
import com.hack.stock2u.chat.repository.MessageChatMongoRepository;
import com.hack.stock2u.models.Reservation;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

  //  채팅방 생성 로직: 누가 방팠는지는 별로 안중요한듯.
  private final MessageChatMongoRepository messageChatMongoRepository;
  private final JpaChatRoomRepository jpaChatRoomRepository;

  @Transactional
  public ReservationRequest createRoom(ChatRoomRequestDto
                                             .CreateReservationRequest createReservationRequest) {
    Reservation newReservation = jpaChatRoomRepository.save(Reservation.builder()
        .product(createReservationRequest.product())
        .seller(createReservationRequest.seller())
        .customer(createReservationRequest.customer())
        .build());
    return ReservationRequest.reserv(newReservation);
  }
  //  특정 채팅방 불러오기 ->>>>>>>>>할일
  //  @Transactional
  //  public List<Reservation> findMyChatRoom(String name) {
  //    Sort sort = Sort.by(Sort.Direction.ASC, "id");
  //    List<Reservation> chatRoomList = this.jpaChatRoomRepository.findAllByName(name, sort);
  //    return new ArrayList<>(chatRoomList);
  //  }
  //
  //
  //  //채팅방 삭제
  //  public void deleteRoom(Long id) {
  //    Reservation reservation = this.jpaChatRoomRepository.findById(id).orElseThrow(
  //        () -> new IllegalArgumentException("해당 chat방 없음" + id));
  //    this.jpaChatRoomRepository.delete(reservation);
  //  }
}
