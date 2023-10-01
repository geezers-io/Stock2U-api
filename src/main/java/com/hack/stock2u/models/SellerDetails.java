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
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Comment("사업자등록번호")
  @Column(name = "license_number")
  private String licenseNumber;

  @Comment("업종")
  @Column
  private String industry;

  @Comment("사업지명")
  @Column(name = "industry_name")
  private String industryName;

  @Comment("소재지")
  @Column
  private String location;

  @Column(name = "bank_name")
  private String bankName;

  @Comment("계좌번호")
  @Column
  private String account;
}
