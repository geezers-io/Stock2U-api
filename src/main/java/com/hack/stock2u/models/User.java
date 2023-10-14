package com.hack.stock2u.models;

import com.hack.stock2u.authentication.dto.AuthRequestDto;
import com.hack.stock2u.constant.AuthVendor;
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
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "users")
@Where(clause = "removed_at IS NULL")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 30, nullable = false)
  private String name;

  private String email;

  @Enumerated(EnumType.STRING)
  private AuthVendor vendor;

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

  public static User signupUser(AuthRequestDto.SignupUserRequest signupUserRequest) {
    User user = new User();
    user.changeName("반려박쥐");
    user.setEmail(signupUserRequest.email());
    user.setPhone(signupUserRequest.phone());
    user.setVendor(signupUserRequest.vendor());
    user.setRole(UserRole.GENERAL);
    BasicDateColumn basicDateColumn = new BasicDateColumn();
    basicDateColumn.setCreatedAt(new Date());
    basicDateColumn.setRemovedAt(null);
    user.setBasicDate(basicDateColumn);
    return user;
  }

  private void setBasicDate(BasicDateColumn basicDate) {
    this.basicDate = basicDate;
  }

  public void changeName(String name) {
    this.name = name;
  }

  private void setEmail(String email) {
    this.email = email;
  }

  private void setPhone(String phone) {
    this.phone = phone;
  }

  private void setVendor(AuthVendor vendor) {
    this.vendor = vendor;
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

}
