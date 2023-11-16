package com.hack.stock2u.product.repository;


import static com.hack.stock2u.models.QProduct.product;

import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.QProduct;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.beans.Expression;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductDslRepository {
  private final JPAQueryFactory factory;


  public List<Tuple> getProductBySellerId(
      Long sellerId,
      Pageable pageable,
      String title,
      boolean isExpired,
      boolean isSelling) {
    Optional<String> optionalTitle = Optional.ofNullable(title);
    JPAQuery<Tuple> query = factory
        .select(
            product.id,
            product.title,
            product.basicDate.createdAt,
            product.type
            )
        .from(product)
        .where(
            product.seller.id.eq(sellerId)
        );
    if (isExpired) {
      query = query.where(Expressions.currentTimestamp().after(product.expiredAt));
    }
    if (isSelling) {
      query = query.where(Expressions.currentTimestamp().between(
        product.basicDate.createdAt,
        product.expiredAt));
    }
    if (optionalTitle.isPresent() && !optionalTitle.get().isEmpty()) {
      query = query.where(product.title.contains(optionalTitle.get()));
    }

    return query
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

  }
}
