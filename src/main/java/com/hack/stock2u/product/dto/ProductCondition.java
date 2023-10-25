package com.hack.stock2u.product.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductCondition {
  private Double latitude;
  private Double longitude;
  private Double distance;
  private List<String> category;
  private Integer minPrice;
  private Integer maxPrice;
}
