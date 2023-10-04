package com.hack.stock2u.models;

import com.hack.stock2u.models.embed.BasicDateColumn;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "reservations")
public class Reservation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Comment("MongoDB Chat 식별자")
  @Column(name = "chat_id")
  private String chatId;

  @Comment("판매자(사업자) id")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_id")
  private User seller;

  @Comment("구매자(예약자) id")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id")
  private User customer;

  @Column(name = "disabled_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date disabledAt;

  @Embedded
  private BasicDateColumn basicDate;
}
