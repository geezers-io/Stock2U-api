package com.hack.stock2u.product.dto;

import com.hack.stock2u.constant.ProductType;
import com.querydsl.core.annotations.QueryProjection;
import java.util.Date;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class ProductSummary {
  private Long id;
  private String title;
  private int price;
  private ProductType type;
  private Date expiredAt;
  private Double distance;
  private Double latitude;
  private Double longitude;
  private String thumbnailUrl;

  @QueryProjection
  public ProductSummary(
      Long id,
      String title,
      int price,
      ProductType type,
      Date expiredAt,
      Double distance,
      Double latitude,
      Double longitude,
      String thumbnailUrl
  ) {
    this.id = id;
    this.title = title;
    this.price = price;
    this.type = type;
    this.expiredAt = expiredAt;
    this.distance = distance;
    this.latitude = latitude;
    this.longitude = longitude;
    this.thumbnailUrl = thumbnailUrl;
  }

  public Long id() {
    return id;
  }

  public String title() {
    return title;
  }

  public int price() {
    return price;
  }

  public ProductType type() {
    return type;
  }

  public Date expiredAt() {
    return expiredAt;
  }

  public Double distance() {
    return distance;
  }

  public Double latitude() {
    return latitude;
  }

  public Double longitude() {
    return longitude;
  }

  public String thumbnailUrl() {
    return thumbnailUrl;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (ProductSummary) obj;
    return Objects.equals(this.id, that.id)
        && Objects.equals(this.title, that.title)
        && this.price == that.price
        && Objects.equals(this.type, that.type)
        && Objects.equals(this.expiredAt, that.expiredAt)
        && Objects.equals(this.distance, that.distance)
        && Objects.equals(this.latitude, that.latitude)
        && Objects.equals(this.longitude, that.longitude)
        && Objects.equals(this.thumbnailUrl, that.thumbnailUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, price, type, expiredAt, distance, latitude, longitude,
        thumbnailUrl);
  }

}
