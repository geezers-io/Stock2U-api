package com.hack.stock2u.chat.service;


import com.hack.stock2u.authentication.AuthException;
import com.hack.stock2u.authentication.dto.SessionUser;
import com.hack.stock2u.authentication.service.SessionManager;
import com.hack.stock2u.chat.dto.ReservationApproveToMessage;
import com.hack.stock2u.chat.dto.ReservationProductPurchaser;
import com.hack.stock2u.chat.dto.request.ChangeStatusRequest;
import com.hack.stock2u.chat.dto.request.ReportRequest;
import com.hack.stock2u.chat.dto.request.ReservationApproveRequest;
import com.hack.stock2u.chat.dto.response.ChatMessageResponse;
import com.hack.stock2u.chat.dto.response.PurchaserSellerReservationsResponse;
import com.hack.stock2u.chat.dto.response.SimpleReservation;
import com.hack.stock2u.chat.dto.response.SimpleThumbnailImage;
import com.hack.stock2u.chat.exception.ReservationException;
import com.hack.stock2u.chat.repository.JpaReportRepository;
import com.hack.stock2u.chat.repository.JpaReservationRepository;
import com.hack.stock2u.chat.repository.MessageChatMongoRepository;
import com.hack.stock2u.constant.ReservationStatus;
import com.hack.stock2u.constant.UserRole;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


@Slf4j
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
  private final ReservationMessageHandler reservationMessageHandler;
  private final MongoTemplate mongoTemplate;
  /**
   * 예약 엔티티를 생성합니다.
   */

  public ReservationProductPurchaser create(Long productId) {

    User purchaser = sessionManager.getSessionUserByRdb();
    Product product = productRepository.findById(productId)
        .orElseThrow(GlobalException.NOT_FOUND::create);

    Date currentDate = new Date();
    Long sellerId = product.getSeller().getId();

    boolean reservationExists =
        reservationRepository.findByBothUserId(purchaser.getId(), sellerId).isPresent();

    if (reservationExists) {
      throw ReservationException.ALREADY_EXISTS.create();
    }

    if (!(product.getExpiredAt().after(currentDate))) {
      throw ReservationException.PRODUCT_EXPIRED.create();
    }
    // 잔여재고 남은 갯수가 0개면 예약되지 않음
    if (product.getProductCount() == 0) {
      throw ReservationException.NOT_ENOUGH_COUNT.create();
    }

    Reservation reservation = Reservation.builder()
        .purchaser(purchaser)
        .product(product)
        .seller(product.getSeller())
        .status(ReservationStatus.REQUESTED)
        .build();
    reservation.setCreateAt(new Date());
    reservationRepository.save(reservation);

    reservationMessageHandler.publishReservationRequest(
        product.getTitle(),
        purchaser.getId(),
        product.getSeller().getId()
    );

    return new ReservationProductPurchaser(reservation, product, purchaser);
  }

  public void cancel(Long id) {
    reservationRepository.deleteById(id);
  }

  public ReservationApproveToMessage approve(ReservationApproveRequest request) {
    Reservation reservation = reservationRepository.findById(request.roomId())
        .orElseThrow(GlobalException.NOT_FOUND::create);
    reservation.changeStatus(ReservationStatus.PROGRESS);
    reservationRepository.save(reservation);
    return new ReservationApproveToMessage(reservation.getStatus(), reservation);
  }

  public Short report(ReportRequest request) {

    SessionUser s = getSessionUser();
    Long id = getUserId(s);
    String r = getUserRole(s);

    Reservation reservation = reservationRepository.findById(request.roomId())
        .orElseThrow(GlobalException.NOT_FOUND::create);
    User target = getWhoIsTarget(r, reservation);

    target.setReportCount();

    User reporter = userRepository.findById(id)
        .orElseThrow(GlobalException.NOT_FOUND::create);

    target.setDisabledDate();

    userRepository.save(target);
    reportRepository.save(Report.builder()
        .reason(request.reason())
        .reporter(reporter)
        .target(target)
        .build()
    );
    return target.getReportCount();
  }

  private User getWhoIsTarget(String r, Reservation reservation) {
    Long userId;
    if (r.equals(UserRole.SELLER.getName())) {
      userId = reservation.getPurchaser().getId();
    } else {
      userId = reservation.getSeller().getId();
    }
    return userRepository.findById(userId)
        .orElseThrow(GlobalException.NOT_FOUND::create);
  }

  public Page<PurchaserSellerReservationsResponse> getReservations(Pageable pageable,
                                                                   String title) {
    SessionUser u = getSessionUser();
    String role = getUserRole(u);
    Long id = getUserId(u);
    String userName = u.username();

    List<Reservation> reservations = getSellerAndPurchaser(id, role, pageable);
    Stream<Reservation> filteredReservations = reservations.stream();

    if (title != null && !title.isEmpty()) {
      filteredReservations =
          filteredReservations.filter(
              reservation -> reservation.getProduct().getTitle().contains(title)
          );
    }

    List<PurchaserSellerReservationsResponse> responses = filteredReservations
        .map(reservation -> createLatestMessageAndThumbnailAndSimpleReservation(
            reservation.getId(), userName))
        .toList();

    return new PageImpl<>(responses);
  }

  public Page<SimpleReservation> getReserveByDate(
      PageRequest pageable, Date startDate, Date endDate) {
    SessionUser u = getSessionUser();
    Long id = getUserId(u);
    String role = getUserRole(u);

    List<Reservation> reservations = getSellerAndPurchaser(id, role, pageable);

    List<SimpleReservation> responses =
        reservations.stream()
        .filter(reservation -> {
          Date creatAt = reservation.getBasicDate().getCreatedAt();
          return creatAt.after(startDate) && creatAt.before(endDate);
        })
        .filter(reservation -> reservation.getStatus() == ReservationStatus.PROGRESS)
        .map(Reservation::getId)
        .map(this::creatSimpleReservation)
        .toList();

    return new PageImpl<>(responses);
  }


  public ReservationStatus changeStatus(ChangeStatusRequest request) {
    //예약 취소, 예약 완료 로직 다르게 짜야함
    Reservation reservation = getReservationId(request.reservationId());
    //setremoveAt을 예약취소
    if (request.status().equals(ReservationStatus.CANCEL.getName())) {
      reservation.setRemoveAt(new Date());
    } else {
      reservation.setRemoveAt(null);
    }
    reservation.changeStatus(ReservationStatus.valueOf(request.status()));

    reservationRepository.save(reservation);
    return reservation.getStatus();
  }

  private SimpleReservation creatSimpleReservation(Long id) {
    Reservation reservation = getReservationId(id);

    SimpleThumbnailImage simpleThumbnailImage = getThumbnailImage(reservation);

    return SimpleReservation.create(reservation, simpleThumbnailImage);
  }

  private SessionUser getSessionUser() {
    return sessionManager.getSessionUser();
  }

  private String getUserRole(SessionUser sessionUser) {
    return sessionUser.userRole().getName();
  }

  private Long getUserId(SessionUser sessionUser) {
    return sessionUser.id();
  }


  private PurchaserSellerReservationsResponse
      createLatestMessageAndThumbnailAndSimpleReservation(Long id, String userName) {

    ChatMessageResponse messageResponse = latestMessage(id);

    Reservation reservation = getReservationId(id);

    SimpleThumbnailImage simpleThumbnailImage = getThumbnailImage(reservation);
    long count = getCountOfMessage(userName, id);
    SimpleReservation simpleReservation = SimpleReservation.create(
        reservation, simpleThumbnailImage);

    return new PurchaserSellerReservationsResponse(messageResponse, simpleReservation, count);
  }

  private long getCountOfMessage(String userName, Long roomId) {
    return chatMongoRepository.countByRoomIdAndReadIsFalseAndUserNameNot(roomId, userName);
  }


  private List<Reservation> getSellerAndPurchaser(Long id, String role, Pageable pageable) {
    return (role.equals(UserRole.SELLER.getName()))
        ? getReservationBySellerId(id, pageable) :
        getReservationByPurchaserId(id, pageable);
  }



  private ChatMessageResponse latestMessage(Long id) {
    ChatMessage chatMessage = chatMongoRepository
        .findTopByRoomIdOrderByCreatedAtDesc(id)
        .orElseThrow(GlobalException.NOT_FOUND::create);

    return ChatMessageResponse.create(chatMessage);
  }

  private SimpleThumbnailImage getThumbnailImage(Reservation reservation) {

    Attach thumbnail = attachRepository.findFirstByProductIdOrderById(
        reservation.getProduct().getId());
    return SimpleThumbnailImage.builder()
        .uploadPath(thumbnail.getUploadPath())
        .build();
  }

  private Reservation getReservationId(Long id) {
    return reservationRepository.findById(id)
        .orElseThrow(GlobalException.NOT_FOUND::create);
  }

  private List<Reservation> getReservationByPurchaserId(Long id, Pageable pageable) {
    return reservationRepository.findByPurchaserId(id, pageable).getContent();
  }

  private List<Reservation> getReservationBySellerId(Long id, Pageable pageable) {
    return reservationRepository.findBySellerId(id, pageable).getContent();
  }

}
