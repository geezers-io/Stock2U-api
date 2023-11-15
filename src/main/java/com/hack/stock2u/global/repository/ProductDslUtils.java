package com.hack.stock2u.global.repository;

import static com.querydsl.core.types.dsl.MathExpressions.acos;
import static com.querydsl.core.types.dsl.MathExpressions.cos;
import static com.querydsl.core.types.dsl.MathExpressions.radians;
import static com.querydsl.core.types.dsl.MathExpressions.sin;

import com.hack.stock2u.models.QAttach;
import com.hack.stock2u.models.QSellerDetails;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Component;

@Component
public class ProductDslUtils {

  /**
   * 위경도를 입력받아 위경도에 존재하는 위치와 판매자의 거리 간 SQL 계산 표현식을 제공합니다
   *
   * @param latitude 위도
   * @param longitude 경도
   */
  public NumberExpression<Double> getDistanceExpression(Double latitude, Double longitude) {
    QSellerDetails sellerDetails = QSellerDetails.sellerDetails;

    NumberExpression<Double> latExp = Expressions.asNumber(latitude);
    NumberExpression<Double> lngExp = Expressions.asNumber(longitude);

    NumberExpression<Double> latRadians = radians(latExp);
    NumberExpression<Double> lngRadians = radians(lngExp);
    NumberExpression<Double> sellerDetailsLatRadians = radians(sellerDetails.latitude);
    NumberExpression<Double> sellerDetailsLngRadians = radians(sellerDetails.longitude);

    NumberExpression<Double> distanceOperation = acos(
        cos(latRadians)
            .multiply(cos(sellerDetailsLatRadians))
            .multiply(cos(sellerDetailsLngRadians.subtract(lngRadians)))
            .add(sin(latRadians).multiply(sin(sellerDetailsLatRadians)))
    )
        .multiply(6379)
        .divide(10);

    return distanceOperation;
  }

  public JPQLQuery<Long> getProductPresentAttachExpression(
      QAttach attach,
      NumberPath<Long> userId,
      NumberPath<Long> productId
  ) {
    return JPAExpressions.select(attach.id.min())
            .from(attach)
            .where(
                attach.user.id.eq(userId),
                attach.product.id.eq(productId)
            )
            .groupBy(attach.id)
            .limit(1);
  }

}
