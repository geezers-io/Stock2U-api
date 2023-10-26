package com.hack.stock2u.chat.service;


import com.hack.stock2u.authentication.service.SessionManager;
import com.hack.stock2u.chat.dto.request.ReservationApproveRequest;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {


  private final MessageChatMongoRepository chatMongoRepository;
  private final JpaReservationRepository reservationRepository;
  private final JpaUserRepository userRepository;
  private final JpaProductRepository productRepository;
  private final JpaAttachRepository attachRepository;
  private final SessionManager sessionManager;

  public Long createRoom(
      ReservationRequestDto.CreateReservationRequest createRequest
  ) {
    User purchaser = userRepository.findById(createRequest.purchaserId())
        .orElseThrow(UserException.NOT_FOUND_USER::create);

    Product product = productRepository.findById(createRequest.productId())
        .orElseThrow(GlobalException.NOT_FOUND::create);

    String roomId = UUID.randomUUID().toString();
    Reservation reservation = reservationRepository.save(Reservation.builder()
        .purchaser(purchaser)
        .product(product)
        .chatId(roomId)
        .seller(product.getSeller())
        .build());
    return reservation.getId();
  }

  public void remove(Long id) {
    Reservation r = reservationRepository.findById(id)
        .orElseThrow(GlobalException.NOT_FOUND::create);
    r.setDisabledAt();
    reservationRepository.save(r);
  }

  public void approve(ReservationApproveRequest request) {

    Product product = productRepository.findById(request.productId())
        .orElseThrow(GlobalException.NOT_FOUND::create);
    product.changeStatus(ReservationStatus.PROGRESS);
    productRepository.save(product);
  }

  public ReservationResponse search(String productName) {
    List<Reservation> reservationList = reservationRepository.findByNameContaining(productName);

    return null;
  }

}
