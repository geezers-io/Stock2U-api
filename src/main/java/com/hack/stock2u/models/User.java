package com.hack.stock2u.models;

import com.hack.stock2u.constant.UserRole;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 30, nullable = false)
  private String name;

  private String email;

  @Column(length = 20, nullable = false)
  private String phone;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private UserRole role;

  @Column(name = "avatar_id")
  private Long avatarId;

  @Column(name = "withdraw_reason")
  private String withdrawReason;

  @Column(name = "report_count", nullable = false)
  private short reportCount;

  @Column(name = "disabled_at")
  private Date disabledAt;

  @Embedded
  private BasicDateColumn basicDate;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_details_id")
  private SellerDetails sellerDetails;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "buyer")
  private List<Buyer> buyers;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  private List<Attach> attaches;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  private List<Product> products;
}
