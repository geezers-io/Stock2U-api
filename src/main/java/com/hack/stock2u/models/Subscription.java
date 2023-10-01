package com.hack.stock2u.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "subscriptions")
public class Subscription {
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Comment("판매자(사업자) id")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "subscriber_id")
  private User subscriber;

  @Comment("구매자(예약자) id")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "target_id")
  private User target;
}
