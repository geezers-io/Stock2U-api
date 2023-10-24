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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "attachments")
public class Attach {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(length = 15)
  private String ext;

  @Column(name = "upload_path", length = 500)
  private String uploadPath;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

  public void setProduct(Product product) {
    this.product = product;
  }

  @Builder
  public Attach(String name, String ext, String uploadPath, User user) {
    this.name = name;
    this.ext = ext;
    this.uploadPath = uploadPath;
    this.user = user;
  }
}
