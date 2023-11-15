package com.hack.stock2u.chat.repository;

import com.hack.stock2u.global.repository.ProductDslUtils;
import com.hack.stock2u.models.QAttach;
import com.hack.stock2u.models.QProduct;
import com.hack.stock2u.models.QReservation;
import com.hack.stock2u.models.QSellerDetails;
import com.hack.stock2u.models.QUser;
import com.hack.stock2u.product.dto.ProductSummary;
import com.hack.stock2u.product.dto.QProductSummary;
import com.hack.stock2u.user.dto.QSellerDetailsLatLng;
import com.hack.stock2u.user.dto.SellerDetailsLatLng;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ReservationProductDslRepository {
  private final JPAQueryFactory queryFactory;
  private final ProductDslUtils productDslUtils;

  public ProductSummary getProductSummaryByReservationId(
      Long reservationId,
      Double lat,
      Double lng
  ) {
    QProduct product = QProduct.product;
    QSellerDetails sellerDetails = QSellerDetails.sellerDetails;
    QUser user = QUser.user;
    QAttach attach = QAttach.attach;
    QAttach subAttach = QAttach.attach;
    QReservation reservation = QReservation.reservation;

    NumberExpression<Double> distanceExp = productDslUtils.getDistanceExpression(lat, lng);

    return queryFactory.select(
        new QProductSummary(
            product.id,
            product.title,
            product.price,
            product.type,
            product.expiredAt,
            distanceExp.as("distance"),
            sellerDetails.latitude,
            sellerDetails.longitude,
            subAttach.uploadPath
        )
    )
        .from(product)
        .leftJoin(reservation).on(reservation.id.eq(reservationId))
        .leftJoin(user).on(user.id.eq(reservation.seller.id))
        .leftJoin(sellerDetails).on(sellerDetails.id.eq(reservation.seller.sellerDetails.id))
        .leftJoin(attach).on(
            attach.id.eq(
                productDslUtils.getProductPresentAttachExpression(
                    attach, reservation.seller.id, product.id
                )
            )
        )
        .where(product.seller.id.eq(reservation.seller.id))
        .fetchFirst();
  }

  public SellerDetailsLatLng getSellerDetailsLatLngByReservationId(Long reservationId) {
    QSellerDetails sellerDetails = QSellerDetails.sellerDetails;
    QReservation reservation = QReservation.reservation;
    QUser user = QUser.user;

    return queryFactory.select(
            new QSellerDetailsLatLng(
              sellerDetails.id,
              sellerDetails.latitude,
              sellerDetails.longitude
            )
        )
        .from(sellerDetails)
        .innerJoin(user).on(user.sellerDetails.id.eq(sellerDetails.id))
        .innerJoin(reservation).on(user.id.eq(reservation.seller.id))
        .where(reservation.id.eq(reservationId))
        .fetchFirst();
  }

}
