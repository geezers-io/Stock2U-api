package com.hack.stock2u.models;

import com.hack.stock2u.constant.ReservationStatus;
import com.hack.stock2u.models.embed.BasicDateColumn;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "reservations")
@AllArgsConstructor
@Builder
public class Reservation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Comment("MongoDB Chat 식별자")
  @Column(name = "chat_id")
  private String chatId;

  @Comment("잔여 재고")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

  @Comment("잔여 재고 이름")
  @Column(name = "product_name")
  private String name;

  @Comment("판매자(사업자) id")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_id")
  private User seller;

  @Comment("구매자 id")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id")
  private User purchaser;

  @Column(name = "disabled_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date disabledAt;

  @Enumerated(EnumType.STRING)
  private ReservationStatus status;

  @Embedded
  private BasicDateColumn basicDate;

  public void setDisabledAt() {
    basicDate.setRemovedAt(new Date());
  }

  @Builder
  public Reservation(String chatId, Product product, String name, User seller, User purchaser) {
    this.chatId = chatId;
    this.product = product;
    this.name = name;
    this.seller = seller;
    this.purchaser = purchaser;
  }

  public void changeStatus(ReservationStatus status) {
    this.status = status;
  }

}
