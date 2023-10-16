package com.hack.stock2u.authentication.service;

import com.hack.stock2u.global.exception.GlobalException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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
    log.error("ACCESS DENIED");
    accessDeniedException.printStackTrace();
    throw GlobalException.FORBIDDEN.create();
  }
}
