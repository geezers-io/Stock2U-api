package com.hack.stock2u.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hack.stock2u.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JsonSerializer {
  private final ObjectMapper objectMapper;

  public Object serialize(Object o) {
    try {
      return objectMapper.writeValueAsString(o);
    } catch (JsonProcessingException e) {
      log.error(e.toString());
      e.printStackTrace();
      throw GlobalException.SERVER_ERROR.create();
    }
  }

}
