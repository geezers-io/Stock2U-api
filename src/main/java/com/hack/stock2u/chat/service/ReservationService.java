package com.hack.stock2u.chat.service;


import com.hack.stock2u.authentication.service.SessionManager;
import com.hack.stock2u.chat.dto.ReservationProductPurchaser;
import com.hack.stock2u.chat.dto.request.ReportRequest;
import com.hack.stock2u.chat.dto.request.ReservationApproveRequest;
import com.hack.stock2u.chat.dto.response.ChatMessageResponse;
import com.hack.stock2u.chat.dto.response.PurchaserReservationsResponse;
import com.hack.stock2u.chat.dto.response.SimpleReservation;
import com.hack.stock2u.chat.dto.response.SimpleThumbnailImage;
import com.hack.stock2u.chat.exception.ReservationException;
import com.hack.stock2u.chat.repository.JpaReportRepository;
import com.hack.stock2u.chat.repository.JpaReservationRepository;
import com.hack.stock2u.chat.repository.MessageChatMongoRepository;
import com.hack.stock2u.constant.ReservationStatus;
import com.hack.stock2u.file.repository.JpaAttachRepository;
import com.hack.stock2u.global.exception.GlobalException;
import com.hack.stock2u.models.Attach;
import com.hack.stock2u.models.ChatMessage;
import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.Report;
import com.hack.stock2u.models.Reservation;
import com.hack.stock2u.models.User;
import com.hack.stock2u.product.repository.JpaProductRepository;
import com.hack.stock2u.user.repository.JpaUserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
  private final JpaReportRepository reportRepository;

  /**
   * 예약 엔티티를 생성합니다.
   */
  public ReservationProductPurchaser create(Long productId) {
    User purchaser = sessionManager.getSessionUserByRdb();
    Product product = productRepository.findById(productId)
        .orElseThrow(GlobalException.NOT_FOUND::create);

    // 잔여재고 남은 갯수가 0개면 예약되지 않음
    if (product.getProductCount() == 0) {
      throw ReservationException.NOT_ENOUGH_COUNT.create();
    }

    String roomId = RandomStringUtils.randomAlphabetic(8);
    Reservation reservation = reservationRepository.save(
        Reservation.builder()
            .purchaser(purchaser)
            .product(product)
            .chatId(roomId)
            .seller(product.getSeller())
            .build()
    );

    return new ReservationProductPurchaser(reservation, product, purchaser);
  }

  public void cancel(Long id) {
    Reservation r = reservationRepository.findById(id)
        .orElseThrow(GlobalException.NOT_FOUND::create);
    r.setDisabledAt();
    reservationRepository.save(r);
  }

  public ReservationStatus approve(ReservationApproveRequest request) {
    Reservation reservation = reservationRepository.findById(request.roomId())
        .orElseThrow(GlobalException.NOT_FOUND::create);
    reservation.changeStatus(ReservationStatus.PROGRESS);
    reservationRepository.save(reservation);
    return ReservationStatus.PROGRESS;
  }

  public Short report(ReportRequest request) {
    User reporter = userRepository.findById(request.reporterId())
        .orElseThrow(GlobalException.NOT_FOUND::create);
    User target = userRepository.findById(request.targetId())
        .orElseThrow(GlobalException.NOT_FOUND::create);

    target.setReportCount();
    userRepository.save(target);
    reportRepository.save(Report.builder()
        .reason(request.reason())
        .reporter(reporter)
        .target(target)
        .build()
    );
    return target.getReportCount();
  }


  public Page<PurchaserReservationsResponse> getPurchaserReservations(Pageable pageable) {

    Long pid = sessionManager.getSessionUser().id();
    List<Reservation> reservations = reservationRepository.findBySellerId(pid, pageable)
        .getContent();

    List<PurchaserReservationsResponse> responses = reservations.stream()
        .map(Reservation::getId)
        .map(this::purchaserReservationLatestMessages)
        .toList();
    return new PageImpl<>(responses);
  }

  private PurchaserReservationsResponse purchaserReservationLatestMessages(Long id) {
    ChatMessage chatMessage = chatMongoRepository
        .findByRoomIdOrderByCreatedAtDesc(id, PageRequest.of(0, 1))
         .orElseThrow(GlobalException.NOT_FOUND::create);
    ChatMessageResponse messageResponse = ChatMessageResponse.create(chatMessage);
    Reservation reservation = reservationRepository.findById(id)
         .orElseThrow(GlobalException.NOT_FOUND::create);
    Attach thumbnail = attachRepository.findFirstByProductIdOrderById(
        reservation.getProduct().getId());
    SimpleThumbnailImage simpleThumbnailImage = SimpleThumbnailImage.builder()
        .uploadPath(thumbnail.getUploadPath())
        .build();
    SimpleReservation simpleReservation = SimpleReservation.create(
        reservation, simpleThumbnailImage);
    return new PurchaserReservationsResponse(messageResponse, simpleReservation);
  }

}
