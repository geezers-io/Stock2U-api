package com.hack.stock2u.authentication.service;

import com.hack.stock2u.constant.AuthVendor;
import org.springframework.core.convert.converter.Converter;

public class AuthVendorEnumConverter implements Converter<String, AuthVendor> {

  @Override
  public AuthVendor convert(String source) {
    return AuthVendor.findByName(source);
  }

}
