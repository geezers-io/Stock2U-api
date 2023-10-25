package com.hack.stock2u.models;

import com.hack.stock2u.constant.ProductType;
import com.hack.stock2u.constant.ReservationStatus;
import com.hack.stock2u.models.embed.BasicDateColumn;
import com.hack.stock2u.product.dto.ProductRequest;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "products")
@Where(clause = "removed_at IS NULL")
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User seller;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
  private List<ProductImage> productImages;

  @OneToMany(fetch = FetchType.LAZY)
  private List<Reservation> reservations;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.PERSIST)
  private List<Attach> attaches;

  @Embedded
  private BasicDateColumn basicDate;

  @Builder
  public Product(String title, String name, int price, ProductType type, String description,
                 boolean onlyOneReserve, boolean showAccountDetails, Date expiredAt,
                 User seller, BasicDateColumn basicDate) {
    this.title = title;
    this.name = name;
    this.price = price;
    this.type = type;
    this.basicDate = getBasicDate();
    this.seller = seller;
    this.description = description;
    this.onlyOneReserve = onlyOneReserve;
    this.showAccountDetails = showAccountDetails;
    this.expiredAt = expiredAt;
  }

  public static Product fromRequest(ProductRequest.Create create, User u) {
    BasicDateColumn basicDateColumn = new BasicDateColumn(new Date(), null);
    return Product.builder()
        .title(create.title())
        .name(create.name())
        .price(create.price())
        .type(create.type())
        .description(create.description())
        .onlyOneReserve(create.onlyOneReserve())
        .showAccountDetails(create.showAccountDetails())
        .expiredAt(create.expiredAt())
        .seller(u)
        .basicDate(basicDateColumn)
        .build();
  }

  public void update(ProductRequest.Create request, List<Attach> images) {
    this.title = request.title();
    this.name = request.name();
    this.price = request.price();
    this.type = request.type();
    this.description = request.description();
    this.onlyOneReserve = request.onlyOneReserve();
    this.showAccountDetails = request.showAccountDetails();
    this.expiredAt = request.expiredAt();
    this.attaches = images;
  }

  public void changeStatus(ReservationStatus status) {
    this.status = status;
  }

  public void setAttaches(List<Attach> attaches) {
    this.attaches = attaches;
  }

  public void remove() {
    basicDate.setRemovedAt(new Date());
  }

}
