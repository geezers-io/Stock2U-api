package com.hack.stock2u.chat.service;


import com.hack.stock2u.chat.dto.request.ChatRoomRequestDto;
import com.hack.stock2u.chat.dto.response.ReservationResponse;
import com.hack.stock2u.chat.repository.JpaChatRoomRepository;
import com.hack.stock2u.chat.repository.MessageChatMongoRepository;
import com.hack.stock2u.file.repository.JpaAttachRepository;
import com.hack.stock2u.global.exception.GlobalException;
import com.hack.stock2u.models.Attach;
import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.ProductImage;
import com.hack.stock2u.models.Reservation;
import com.hack.stock2u.models.User;
import com.hack.stock2u.product.repository.JpaProductRepository;
import com.hack.stock2u.user.UserException;
import com.hack.stock2u.user.repository.JpaUserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

  //  채팅방 생성 로직: 누가 방팠는지는 별로 안중요한듯.
  private final MessageChatMongoRepository messageChatMongoRepository;
  private final JpaChatRoomRepository jpaChatRoomRepository;
  private final JpaUserRepository userRepository;
  private final JpaProductRepository productRepository;
  private final JpaAttachRepository attachRepository;


  public ReservationResponse createRoom(
      ChatRoomRequestDto.CreateReservationRequest createRequest
  ) {
    User purchaser = userRepository.findById(createRequest.purchaserId())
        .orElseThrow(UserException.NOT_FOUND_USER::create);
    Product product = productRepository.findById(createRequest.productId())
        .orElseThrow(GlobalException.NOT_FOUND::create);
    List<Attach> attaches = product.getProductImages().stream()
        .map(ProductImage::getAttach)
        .toList();
    Reservation newReservation = jpaChatRoomRepository.save(Reservation.builder()
        .purchaser(purchaser)
        .product(product)
        .seller(product.getSeller())
        .build());

    return ReservationResponse.reserv(newReservation, attaches);
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
