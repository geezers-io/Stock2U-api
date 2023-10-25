package com.hack.stock2u.chat.service;


import com.hack.stock2u.authentication.service.SessionManager;
import com.hack.stock2u.chat.dto.ReservationApproveDto;
import com.hack.stock2u.chat.dto.request.ReservationRequestDto;
import com.hack.stock2u.chat.dto.response.ReservationResponse;
import com.hack.stock2u.chat.repository.JpaReservationRepository;
import com.hack.stock2u.chat.repository.MessageChatMongoRepository;
import com.hack.stock2u.constant.ReservationStatus;
import com.hack.stock2u.file.repository.JpaAttachRepository;
import com.hack.stock2u.global.exception.GlobalException;
import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.Reservation;
import com.hack.stock2u.models.User;
import com.hack.stock2u.product.repository.JpaProductRepository;
import com.hack.stock2u.user.UserException;
import com.hack.stock2u.user.repository.JpaUserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

  //  채팅방 생성 로직: 누가 방팠는지는 별로 안중요한듯.
  private final MessageChatMongoRepository chatMongoRepository;
  private final JpaReservationRepository reservationRepository;
  private final JpaUserRepository userRepository;
  private final JpaProductRepository productRepository;
  private final JpaAttachRepository attachRepository;
  private final SessionManager sessionManager;
  //처음에는 채팅방만 만들고
  //예약되었을때만 이제 예약이 생성되는느낌? 기존에는 chatroomid만 만들고
  //나중에 chatroomid가지고 그걸로 예약이 되었을때 그 안에다가 추가해서 완성하는느낌으로

  public void createRoom(
      ReservationRequestDto.CreateReservationRequest createRequest
  ) {
    User purchaser = userRepository.findById(createRequest.purchaserId())
        .orElseThrow(UserException.NOT_FOUND_USER::create);

    Product product = productRepository.findById(createRequest.productId())
        .orElseThrow(GlobalException.NOT_FOUND::create);
    User seller = userRepository.findById(product.getSeller().getId())
        .orElseThrow(UserException.NOT_FOUND_USER::create);
    //채팅방 아이디 저장해야함 : 성공
    String roomId = UUID.randomUUID().toString();
    reservationRepository.save(Reservation.builder()
        .purchaser(purchaser)
        .product(product)
        .chatId(roomId)
        .seller(seller)
        .build());
  }

  public void remove(Long id) {
    Reservation r = reservationRepository.findById(id)
        .orElseThrow(GlobalException.NOT_FOUND::create);
    r.setDisabledAt();
    reservationRepository.save(r);
  }

  public void approve(ReservationApproveDto approveDto) {

    Product product = productRepository.findById(approveDto.productId())
        .orElseThrow(GlobalException.NOT_FOUND::create);
    product.changeStatus(ReservationStatus.PROGRESS);
    productRepository.save(product);


  }

  public ReservationResponse search(String productName){
    List<Reservation> reservationList = reservationRepository.findByNameContaining(productName);

    return null;
  }

  public Page<ReservationResponse> getReservations(Pageable pageable) {
    return reservationRepository.getReservations(pageable);
  }
  //보류 내가 가진 채팅방 가져오기

//  public ReservationResponseDto.GetReservation findMyReservation() {
//    SessionUser sessionUser = sessionManager.getSessionUser();
//
//    if (sessionUser.userRole() == UserRole.PURCHASER) {
//      List<Reservation> byPurchaserId = reservationRepository.findByPurchaserId(sessionUser.id());
//      Product product = productRepository.findById(sessionUser.id())
//          .orElseThrow(GlobalException.NOT_FOUND::create);
//      List<Attach> attaches = product.getProductImages().stream()
//          .map(ProductImage::getAttach)
//          .toList();
//      return ReservationResponseDto.GetReservation.reserv(byPurchaserId, product, attaches);
//    } else if (sessionUser.userRole() == UserRole.SELLER) {
//      List<Reservation> bySellerId = reservationRepository.findBySellerId(sessionUser.id());
//      Product product = productRepository.findById(sessionUser.id())
//          .orElseThrow(GlobalException.NOT_FOUND::create);
//      List<Attach> attaches = product.getProductImages().stream()
//          .map(ProductImage::getAttach)
//          .toList();
//      return ReservationResponseDto.GetReservation.reserv(bySellerId, product, attaches);
//    }
//    return null;
//  }
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
