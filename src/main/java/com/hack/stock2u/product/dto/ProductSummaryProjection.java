package com.hack.stock2u.product.dto;

import com.hack.stock2u.constant.ProductType;
import java.util.Date;

public interface ProductSummaryProjection {
  Long getId();

  String getTitle();

  int getPrice();

  ProductType getProductType();

  Date getExpiredAt();

  Double getDistance();

  Double getLatitude();

  Double getLongitude();

  String getThumbnailUrl();

  Date getCreatedAt();
}
