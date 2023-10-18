package com.hack.stock2u.global.filter;

import com.hack.stock2u.utils.RoleGuard;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class RoleGuardInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler
  ) throws Exception {
    HandlerMethod handlerMethod = (HandlerMethod) handler;
    RoleGuard roleGuard = handlerMethod.getMethodAnnotation(RoleGuard.class);
    if (roleGuard == null) {
      return true;
    }

    HttpSession session = request.getSession();
    log.debug("{}", session.getAttributeNames());
    return false;
  }
}
