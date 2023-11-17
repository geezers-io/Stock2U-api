package com.hack.stock2u.chat.repository;

import static com.hack.stock2u.models.QReservation.reservation;

import com.hack.stock2u.chat.dto.response.SimpleReservation;
import com.hack.stock2u.constant.ReservationStatus;
import com.hack.stock2u.constant.UserRole;
import com.hack.stock2u.models.Reservation;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ReservationDslRepository {
  private final JPAQueryFactory queryFactory;

  public List<Reservation> findByProductId(
      Long productId,
      Pageable pageable,
      boolean isReservedProduct,
      boolean isCompletedProduct,
      boolean isCanceledProduct
  ) {
    JPAQuery<Reservation> query = queryFactory.selectFrom(reservation)
        .where(reservation.product.id.eq(productId));
    if (isReservedProduct) {
      query = query.where(reservation.status.eq(ReservationStatus.PROGRESS));
    }
    if (isCompletedProduct) {
      query = query.where(reservation.status.eq(ReservationStatus.COMPLETION));
    }
    if (isCanceledProduct) {
      query = query.where(reservation.status.eq(ReservationStatus.CANCEL));
    }
    return query
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
  }

  public List<Tuple> findByDate(
      Long userId,
      UserRole userRole,
      Pageable pageable,
      Date startDate,
      Date endDate
  ) {

    JPAQuery<Tuple> query = queryFactory.select(
        reservation.id,
        reservation.product.title,
        reservation.product.name,
        reservation.status,
        reservation.product.id
    ).from(reservation);
    if (userRole.equals(UserRole.SELLER)) {
      query = query.where(reservation.seller.id.eq(userId));

    }
    if (userRole.equals(UserRole.PURCHASER)) {
      query = query.where(reservation.purchaser.id.eq(userId));
    }
    query = query.where(searchDateFilter(startDate, endDate)
        .and(reservation.status.eq(ReservationStatus.PROGRESS)));
    return query
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
  }

  private BooleanExpression searchDateFilter(Date startDate, Date endDate) {
    LocalDateTime startDateTime = convertToLocalDateTimeWithStartOfDay(startDate);
    LocalDateTime endDateTime = convertToLocalDateTimeWithEndOfDay(endDate);

    Date convertStart = convertToDate(startDateTime);
    Date convertEnd = convertToDate(endDateTime);
    BooleanExpression isGoeStartDate = reservation.basicDate.createdAt.after(convertStart);
    BooleanExpression isLoeEndDate = reservation.basicDate.createdAt.before(convertEnd);

    return Expressions.allOf(isGoeStartDate, isLoeEndDate);
  }

  private LocalDateTime convertToLocalDateTimeWithStartOfDay(Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
  }

  private LocalDateTime convertToLocalDateTimeWithEndOfDay(Date date) {
    return date.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .atTime(LocalTime.MAX)
        .withNano(0);
  }

  private Date convertToDate(LocalDateTime dateTime) {
    return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

}
