package com.hack.stock2u.utils;

import com.hack.stock2u.constant.ProductType;
import org.springframework.core.convert.converter.Converter;

public class ProductTypeEnumConverter implements Converter<String, ProductType> {
  @Override
  public ProductType convert(String source) {
    return ProductType.findByName(source);
  }
}
