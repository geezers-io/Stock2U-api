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

  @Comment("잔여 재고")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

  @Comment("판매자(사업자) id")
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "seller_id")
  private User seller;

  @Comment("구매자 id")
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "purchaser_id")
  private User purchaser;

  @Enumerated(EnumType.STRING)
  private ReservationStatus status;

  @Embedded
  private BasicDateColumn basicDate;

  public void setRemoveAt(Date date) {
    this.basicDate.setRemovedAt(date);
  }

  public void setCreateAt(Date date) {
    basicDate = new BasicDateColumn();
    basicDate.setCreatedAt(date);
  }

  public Date getRemovedAt() {
    return this.basicDate.getRemovedAt();
  }

  @Builder
  public Reservation(Product product,
                      User seller, User purchaser) {
    this.product = product;
    this.seller = seller;
    this.purchaser = purchaser;
  }

  public void changeStatus(ReservationStatus status) {
    this.status = status;
  }

}
