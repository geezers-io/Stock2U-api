package com.hack.stock2u.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hack.stock2u.constant.ProductType;
import java.util.Date;

public interface ProductCountProjection {
  Integer getTotalCount();

  Double getDistance();
}
