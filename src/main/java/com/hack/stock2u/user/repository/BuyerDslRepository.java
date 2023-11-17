package com.hack.stock2u.user.repository;

import static com.hack.stock2u.models.QBuyer.buyer;

import com.hack.stock2u.models.Buyer;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BuyerDslRepository {
  private final JPAQueryFactory queryFactory;


  public List<Buyer> findByPurchaserId(
      Long userId,
      Date start,
      Date end,
      Pageable pageable
  ) {
    JPAQuery<Buyer> query = queryFactory.selectFrom(buyer)
        .where(buyer.purchaser.id.eq(userId));

    LocalDateTime startLocalDate = new LocalDateTime(start);
    LocalDateTime endLocalDate = new LocalDateTime(end);

    boolean isDateSame = startLocalDate.equals(endLocalDate);

    if (isDateSame) {
      DateTime time = new DateTime()
          .withYear(startLocalDate.getYear())
          .withMonthOfYear(endLocalDate.getMonthOfYear())
          .withDayOfMonth(endLocalDate.getDayOfMonth())
          .withHourOfDay(23)
          .withMinuteOfHour(59)
          .withSecondOfMinute(59);
      endLocalDate = new LocalDateTime(time);
    }

    if (start != null && end != null) {
      query.where(buyer.createdAt.between(startLocalDate.toDate(), endLocalDate.toDate()));
    }

    return query
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
  }

  private String convertDate(LocalDateTime date) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return formatter.format(date);
  }
}
