package com.hack.stock2u.authentication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hack.stock2u.global.exception.GlobalException;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthAccessDeniedHandler implements AccessDeniedHandler {
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
                     AccessDeniedException accessDeniedException)
      throws IOException, ServletException {
    HashMap<String, String> map = new HashMap<>();
    map.put("errorCode", "FORBIDDEN");
    map.put("message", "접근 권한이 없습니다.");
    log.error("ACCESS DENIED");
    accessDeniedException.printStackTrace();

    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter()
        .write(new ObjectMapper().writeValueAsString(map));
  }
}
