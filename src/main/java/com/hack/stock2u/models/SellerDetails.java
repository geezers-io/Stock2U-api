package com.hack.stock2u.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hack.stock2u.user.dto.SellerRequest;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "seller_details")
public class SellerDetails implements Serializable {
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

  @Column(precision = 18, scale = 10)
  @Comment("위도")
  private Double latitude;

  @Column(precision = 18, scale = 10)
  @Comment("경도")
  private Double longitude;

  @JsonBackReference
  @OneToOne(mappedBy = "sellerDetails")
  private User user;

  @Builder
  public SellerDetails(String licenseNumber, String industry, String industryName, String location,
                       String bankName, String account, Double latitude, Double longtitude) {
    this.licenseNumber = licenseNumber;
    this.industry = industry;
    this.industryName = industryName;
    this.location = location;
    this.bankName = bankName;
    this.account = account;
    this.latitude = latitude;
    this.longitude = longtitude;
  }

  public void changeLocation(String location) {
    this.location = location;
  }

  public void changeBankName(String bankName) {
    this.bankName = bankName;
  }

  public void changeAccount(String account) {
    this.account = account;
  }

  public void changePosition(Double latitude, Double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public void changePosition(SellerRequest.LocationUpdate locationUpdateRequest) {
    this.latitude = locationUpdateRequest.latitude();
    this.longitude = locationUpdateRequest.longitude();
  }
}
