package com.hack.stock2u.models;

import com.hack.stock2u.constant.ProductType;
import com.hack.stock2u.constant.ReservationStatus;
import com.hack.stock2u.models.embed.BasicDateColumn;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "products")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Comment("판매글 제목")
  private String title;

  @Comment("재고 이름")
  private String name;

  private int price;

  @Comment("재고 분류")
  @Enumerated(EnumType.STRING)
  private ProductType type;

  @Comment("재고 설명")
  @Column(length = 1000)
  private String description;

  @Comment("예약 한 건만 받기")
  @Column(name = "only_one_reserve")
  private boolean onlyOneReserve;

  @Comment("계좌 정보 보이기")
  @Column(name = "show_account_details")
  private boolean showAccountDetails;

  @Comment("게시 마감 기한")
  @Column(name = "expired_at")
  private Date expiredAt;

  @Enumerated(EnumType.ORDINAL)
  private ReservationStatus status;

  @Comment("위도")
  private Double latitude;

  @Comment("경도")
  private Double longitude;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User seller;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
  private List<ProductImage> productImages;

  @OneToMany(fetch = FetchType.LAZY)
  private List<Reservation> reservations;

  @Embedded
  private BasicDateColumn basicDate;
}
