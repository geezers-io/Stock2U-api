package com.hack.stock2u.global.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
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
//      HttpServletReqWrapper reqWrapper = new HttpServletReqWrapper(request);
      MDC.put("traceId", traceId);
      String queryString = request.getQueryString();
      String method = request.getMethod();
      String requestUri = request.getRequestURI();

      log.debug("====================== HTTP Request ======================");
      log.debug("[{}] {}", method, requestUri);
      log.debug("URL Params: {}", queryString);

//      if (method.equalsIgnoreCase("POST")) {
//        try {
//          String body = getBody(reqWrapper);
//          //        log.debug("Body: {}", IOUtils.toString(request.getInputStream()));
//          log.debug("Body: {}", body);
//        } catch (Exception ex) {
//          ex.printStackTrace();
//        }
//      }

      response.setHeader("X-Trace-Id", traceId);
      log.debug("========================================================");
      MDC.clear();
    } catch (Exception ex) {
      log.error("error: {}\n{}", ex.getMessage(), ex.getStackTrace());
    } finally {
      filterChain.doFilter(request, response);
    }
  }

  public static String getBody(ServletRequest request) throws IOException {

    String body = null;
    StringBuilder stringBuilder = new StringBuilder();
    BufferedReader bufferedReader = null;
    InputStream inputStream = request.getInputStream();

    if (inputStream != null) {
      bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      char[] charBuffer = new char[128];
      int bytesRead = -1;
      while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
        stringBuilder.append(charBuffer, 0, bytesRead);
      }
    }
    if (bufferedReader != null) {
      bufferedReader.close();
    }

    body = stringBuilder.toString();
    return body;
  }

}
