package com.hack.stock2u.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hack.stock2u.constant.ProductType;
import java.util.Date;

public interface ProductCountProjection {
  Long getTotalCount();

  Double getDistance();
}
