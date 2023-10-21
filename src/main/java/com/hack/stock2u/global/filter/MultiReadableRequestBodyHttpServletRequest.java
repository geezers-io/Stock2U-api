package com.hack.stock2u.global.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;

public class HttpServletReqWrapper extends ServletRequestWrapper implements ServletRequest {
  private final byte[] bodyData;

  public HttpServletReqWrapper(HttpServletRequest request) {
    super(request);

    try {
      InputStream inputStream = request.getInputStream();
      this.bodyData = IOUtils.toByteArray(inputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public BufferedReader getReader() throws IOException {
    return new BufferedReader(new InputStreamReader(this.getInputStream()));
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bodyData);
    return new ServletInputStream() {
      @Override
      public boolean isFinished() {
        return byteArrayInputStream.available() == 0;
      }

      @Override
      public boolean isReady() {
        return true;
      }

      @Override
      public void setReadListener(ReadListener listener) {}

      @Override
      public int read() throws IOException {
        return byteArrayInputStream.read();
      }
    };
  }
}
