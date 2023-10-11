package com.hack.stock2u.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "seller_details")
public class SellerDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Comment("사업자등록번호")
  @Column(name = "license_number", length = 40)
  private String licenseNumber;

  @Comment("업종")
  @Column(length = 100)
  private String industry;

  @Comment("사업지명")
  @Column(name = "industry_name", length = 50)
  private String industryName;

  @Comment("소재지")
  private String location;

  @Column(name = "bank_name", length = 30)
  private String bankName;

  @Comment("계좌번호")
  @Column(length = 50)
  private String account;
}
