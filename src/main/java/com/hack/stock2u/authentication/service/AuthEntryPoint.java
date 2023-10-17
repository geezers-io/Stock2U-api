package com.hack.stock2u.authentication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hack.stock2u.global.exception.BasicErrorResponse;
import com.hack.stock2u.global.exception.BasicException;
import com.hack.stock2u.global.exception.GlobalException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       AuthenticationException authException) throws IOException, ServletException {

    log.error("UNAUTHORIZED: {}", authException.getMessage());
    authException.printStackTrace();
    BasicException ex = GlobalException.UNAUTHORIZED.create();
    BasicErrorResponse res = ex.getErrorResponse();
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    String value = new ObjectMapper().writeValueAsString(res);
    response.getWriter().write(value);
  }
}
