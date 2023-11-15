package com.hack.stock2u.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.Objects;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SellerDetailsLatLng {
  private Long id;
  private Double latitude;
  private Double longitude;

  @QueryProjection
  public SellerDetailsLatLng(
      Long id,
      Double latitude,
      Double longitude
  ) {
    this.id = id;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public Long id() {
    return id;
  }

  public Double latitude() {
    return latitude;
  }

  public Double longitude() {
    return longitude;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (SellerDetailsLatLng) obj;
    return Objects.equals(this.id, that.id) &&
        Objects.equals(this.latitude, that.latitude) &&
        Objects.equals(this.longitude, that.longitude);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, latitude, longitude);
  }

  @Override
  public String toString() {
    return "SellerDetailsLatLng[" + "id=" + id + ", "
        + "latitude=" + latitude + ", "
        + "longitude=" + longitude + ']';
  }
}
