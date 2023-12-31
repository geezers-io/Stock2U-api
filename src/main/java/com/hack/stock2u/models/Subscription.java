package com.hack.stock2u.models;

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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Comment("구매자 id")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "subscriber_id")
  private User subscriber;

  @Comment("판매자 id")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "target_id")
  private User target;

  public Subscription(User subscriber, User target) {
    this.subscriber = subscriber;
    this.target = target;
  }

}
