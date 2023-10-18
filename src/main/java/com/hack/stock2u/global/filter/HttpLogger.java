package com.hack.stock2u.global.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class HttpLogger extends OncePerRequestFilter {
  private final List<String> excludeAntPaths = Arrays.asList(
      "/api/swagger-ui/**", "/api/docs/**", "/api/api-docs/**"
  );

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String requestUri = request.getRequestURI();
    AntPathMatcher matcher = new AntPathMatcher();

    for (var path : excludeAntPaths) {
      boolean match = matcher.match(path, requestUri);
      if (match) {
        return true;
      }
    }

    return false;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
  ) throws ServletException, IOException {
    try {
      String traceId = RandomStringUtils.randomAlphabetic(8);
      MDC.put("traceId", traceId);

      String method = request.getMethod();
      String requestUri = request.getRequestURI();

      log.debug("[{}] {}", method, requestUri);
      response.setHeader("X-Trace-Id", traceId);
      MDC.clear();
    } catch (Exception ex) {
      log.error("error: {}\n{}", ex.getMessage(), ex.getStackTrace());
    } finally {
      filterChain.doFilter(request, response);
    }

  }
}
