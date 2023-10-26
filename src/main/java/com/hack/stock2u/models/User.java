package com.hack.stock2u.models;

import com.hack.stock2u.authentication.dto.AuthRequestDto;
import com.hack.stock2u.constant.AuthVendor;
import com.hack.stock2u.constant.UserRole;
import com.hack.stock2u.models.embed.BasicDateColumn;
import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "users")
@Where(clause = "removed_at IS NULL")
public class User implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 15, nullable = false)
  private String name;

  private String email;

  @Column(name = "oauth_id", nullable = false)
  private String oauthId;

  @Enumerated(EnumType.STRING)
  private AuthVendor vendor;

  @Column(length = 11, nullable = false)
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

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "seller_details_id")
  private SellerDetails sellerDetails;  

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaser")
  private List<Buyer> buyers;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  private List<Attach> attaches;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "seller")
  private List<Product> products;

  @Builder
  public User(String name, String email, AuthVendor vendor, String phone, BasicDateColumn basicDate,
              SellerDetails sellerDetails, String oauthId) {
    this.name = name;
    this.email = email;
    this.vendor = vendor;
    this.phone = phone;
    this.basicDate = basicDate;
    this.sellerDetails = sellerDetails;
    this.oauthId = oauthId;
  }

  public static User signupPurchaser(AuthRequestDto.SignupPurchaserRequest signupUserRequest) {
    BasicDateColumn date = new BasicDateColumn();
    date.setCreatedAt(new Date());
    date.setRemovedAt(null);

    User user = User.builder()
        .name(signupUserRequest.username())
        .email(signupUserRequest.email())
        .oauthId(signupUserRequest.verification())
        .phone(signupUserRequest.phone())
        .vendor(signupUserRequest.vendor())
        .basicDate(date)
        .build();

    user.setRole(UserRole.PURCHASER);

    return user;
  }

  public static User signupSeller(AuthRequestDto.SignupSellerRequest signupSellerRequest) {
    BasicDateColumn date = new BasicDateColumn();
    date.setCreatedAt(new Date());

    SellerDetails sellerDetails = SellerDetails.builder()
        .licenseNumber(signupSellerRequest.licenseNumber())
        .industry(signupSellerRequest.industry())
        .industryName(signupSellerRequest.industryName())
        .location(signupSellerRequest.location())
        .bankName(signupSellerRequest.bankName())
        .account(signupSellerRequest.account())
        .build();

    User user = User.builder()
        .name(signupSellerRequest.username())
        .email(signupSellerRequest.email())
        .vendor(signupSellerRequest.vendor())
        .phone(signupSellerRequest.phone())
        .basicDate(date)
        .oauthId(signupSellerRequest.verification())
        .sellerDetails(sellerDetails)
        .build();

    user.setRole(UserRole.SELLER);

    return user;
  }

  private void setBasicDate(BasicDateColumn basicDate) {
    this.basicDate = basicDate;
  }

  public void changeName(String name) {
    this.name = name;
  }

  private void changeEmail(String email) {
    this.email = email;
  }

  public void changePhone(String phone) {
    this.phone = phone;
  }


  private void setRole(UserRole role) {
    this.role = role;
  }

  public void remove(String reason) {
    this.withdrawReason = reason;
    BasicDateColumn dateSet = this.getBasicDate();
    dateSet.setRemovedAt(new Date());
    setBasicDate(dateSet);
  }

  public void changeSellerDetails(SellerDetails sellerDetails) {
    this.sellerDetails = sellerDetails;
  }

  public void changeAvatarId(Long id) {
    this.avatarId = id;
  }

  public void setReportCount() {
    this.reportCount += 1;
  }
}
