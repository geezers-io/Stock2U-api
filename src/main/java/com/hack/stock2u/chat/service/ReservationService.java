package com.hack.stock2u.chat.service;


import com.hack.stock2u.authentication.service.SessionManager;
import com.hack.stock2u.chat.dto.ReservationApproveToMessage;
import com.hack.stock2u.chat.dto.ReservationProductPurchaser;
import com.hack.stock2u.chat.dto.request.ChangeStatusRequest;
import com.hack.stock2u.chat.dto.request.ReportRequest;
import com.hack.stock2u.chat.dto.request.ReservationApproveRequest;
import com.hack.stock2u.chat.dto.response.ChatMessageResponse;
import com.hack.stock2u.chat.dto.response.ChatRoomSummary;
import com.hack.stock2u.chat.dto.response.SimpleReservation;
import com.hack.stock2u.chat.dto.response.SimpleThumbnailImage;
import com.hack.stock2u.chat.exception.ReservationException;
import com.hack.stock2u.chat.repository.JpaReportRepository;
import com.hack.stock2u.chat.repository.JpaReservationRepository;
import com.hack.stock2u.chat.repository.MessageChatMongoRepository;
import com.hack.stock2u.chat.repository.ReservationDslRepository;
import com.hack.stock2u.constant.MessageTemplate;
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
import com.querydsl.core.Tuple;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
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
  private final ReservationDslRepository reservationDslRepository;
  /**
   * 예약 엔티티를 생성합니다.
   */

  public ReservationProductPurchaser create(Long productId) {

    User purchaser = sessionManager.getSessionUserByRdb();
    Product product = productRepository.findById(productId)
        .orElseThrow(GlobalException.NOT_FOUND::create);

    Date currentDate = new Date();
    Long sellerId = product.getSeller().getId();

    Optional<Reservation> reservationOptional =
        reservationRepository.findByBothUserIdWithOutCancel(purchaser.getId(), sellerId, productId);
    boolean reservationExists = reservationOptional.isPresent();

    if (reservationExists) {
      boolean isCancel = ReservationStatus.CANCEL.equals(reservationOptional.get().getStatus());
      if (!isCancel) {
        throw ReservationException.ALREADY_EXISTS.create();
      }
    }

    if (!(product.getExpiredAt().after(currentDate))) {
      throw ReservationException.PRODUCT_EXPIRED.create();
    }
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

    //글로벌 알림
    reservationMessageHandler.publishReservation(
        MessageTemplate.RESERVATION_REQUEST,
        product.getTitle(),
        purchaser.getId(),
        product.getSeller().getId()
    );

    return new ReservationProductPurchaser(reservation, purchaser);
  }

  public Reservation cancel(Long id) {
    Reservation reservation = reservationRepository.findById(id)
        .orElseThrow(GlobalException.NOT_FOUND::create);
    //글로벌 알림
    reservationMessageHandler.publishReservation(
        MessageTemplate.RESERVATION_CANCEL,
        reservation.getProduct().getTitle(),
        reservation.getPurchaser().getId(),
        reservation.getSeller().getId()
    );
    reservation.setRemoveAt(new Date());
    reservation.changeStatus(ReservationStatus.CANCEL);
    reservationRepository.save(reservation);
    return reservation;
  }

  public ReservationApproveToMessage approve(ReservationApproveRequest request) {
    Reservation reservation = reservationRepository.findById(request.roomId())
        .orElseThrow(GlobalException.NOT_FOUND::create);
    reservation.changeStatus(ReservationStatus.PROGRESS);
    reservationRepository.save(reservation);
    //글로벌 알림
    reservationMessageHandler.publishReservation(
        MessageTemplate.RESERVATION_APPORVE,
        reservation.getProduct().getTitle(),
        reservation.getPurchaser().getId(),
        reservation.getSeller().getId()
    );

    return new ReservationApproveToMessage(reservation.getStatus(), reservation);
  }

  public Short report(ReportRequest request) {

    User u = sessionManager.getSessionUserByRdb();
    Reservation reservation = reservationRepository.findById(request.roomId())
        .orElseThrow(GlobalException.NOT_FOUND::create);
    User target = getWhoIsTarget(u.getRole().getName(), reservation);
    //이미 신고했으면 410 에러
    reportRepository.findByTargetIdAndReporterId(target.getId(), u.getId())
            .orElseThrow(ReservationException.ALREADY_REPORTED::create);

    target.setReportCount();

    User reporter = userRepository.findById(u.getId())
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

  public Page<ChatRoomSummary> getReservations(Pageable pageable,
                                               String title) {
    User u = sessionManager.getSessionUserByRdb();


    List<Reservation> reservations = getSellerAndPurchaser(
        u.getId(), u.getRole().getName(), pageable);
    Stream<Reservation> filteredReservations = reservations.stream();

    if (title != null && !title.isEmpty()) {
      filteredReservations =
          filteredReservations.filter(
              reservation -> reservation.getProduct().getTitle().contains(title)
          );
    }

    List<ChatRoomSummary> responses = filteredReservations
        .map(reservation -> createLatestMessageAndThumbnailAndSimpleReservation(
            reservation, u.getName()))
        .toList();

    return new PageImpl<>(responses);
  }

  public Page<SimpleReservation> getReserveByDate(
      PageRequest pageable, Date startDate, Date endDate) {

    User u = sessionManager.getSessionUserByRdb();
    List<Tuple> reservations =
        reservationDslRepository.findByDate(u.getId(), u.getRole(), pageable, startDate, endDate);
    List<SimpleReservation> responses =
        reservations.stream()
        .map(this::creatSimpleReservation)
        .toList();
    return new PageImpl<>(responses);
  }


  public ReservationStatus changeStatus(ChangeStatusRequest request) {

    Reservation reservation = getReservationId(request.reservationId());

    if (request.status().equals(ReservationStatus.CANCEL.getName())) {
      reservation.setRemoveAt(new Date());
    }
    if (request.status().equals(ReservationStatus.PROGRESS.getName())) {
      //글로벌 알림
      reservationMessageHandler.publishReservation(
          MessageTemplate.RESERVATION_APPORVE,
          reservation.getProduct().getTitle(),
          reservation.getPurchaser().getId(),
          reservation.getSeller().getId()
      );
    }
    if (request.status().equals(ReservationStatus.COMPLETION.getName())) {
      reservationMessageHandler.publishReservation(
          MessageTemplate.RESERVATION_SUCCESS,
          reservation.getProduct().getTitle(),
          reservation.getPurchaser().getId(),
          reservation.getSeller().getId()
      );
    }
    reservation.changeStatus(ReservationStatus.valueOf(request.status()));

    reservationRepository.save(reservation);
    return reservation.getStatus();
  }

  private SimpleReservation creatSimpleReservation(Tuple tuple) {
    Long productId = tuple.get(4, Long.class);
    SimpleThumbnailImage simpleThumbnailImage = getThumbnailImage(productId);

    return SimpleReservation.builder()
        .id(tuple.get(0, Long.class))
        .title(tuple.get(1, String.class))
        .name(tuple.get(2, String.class))
        .status(tuple.get(3, ReservationStatus.class))
        .uploadUrl(simpleThumbnailImage.uploadPath())
        .build();
  }



  private ChatRoomSummary
      createLatestMessageAndThumbnailAndSimpleReservation(
          Reservation reservation,
          String userName) {

    ChatMessageResponse latestChat = latestMessage(reservation.getId());

    Long productId = reservation.getProduct().getId();
    SimpleThumbnailImage simpleThumbnailImage = getThumbnailImage(productId);

    SimpleReservation reservationSummary = SimpleReservation.create(
        reservation, simpleThumbnailImage);
    long countOfMessage = getCountOfMessage(userName, reservation.getId());

    return new ChatRoomSummary(
        latestChat, reservationSummary, countOfMessage);
  }

  public long getCountOfMessage(String userName, Long roomId) {
    return chatMongoRepository.countByRoomIdAndReadIsFalseAndUsernameNot(roomId, userName);
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

  public SimpleThumbnailImage getThumbnailImage(Long productId) {

    Attach thumbnail = attachRepository.findFirstByProductIdOrderById(
        productId);
    return SimpleThumbnailImage.builder()
        .uploadPath(thumbnail.getUploadPath())
        .build();
  }

  private Reservation getReservationId(Long id) {
    return reservationRepository.findById(id)
        .orElseThrow(GlobalException.NOT_FOUND::create);
  }

  private List<Reservation> getReservationByPurchaserId(Long id, Pageable pageable) {
    return reservationRepository.findByPurchaserIdOrderByBasicDateCreatedAtDesc(
        id, pageable).getContent();
  }

  public List<Reservation> getReservationBySellerId(Long id, Pageable pageable) {
    return reservationRepository.findBySellerIdOrderByBasicDateCreatedAtDesc(
        id, pageable).getContent();
  }

}
