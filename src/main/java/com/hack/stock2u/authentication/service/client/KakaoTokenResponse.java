package com.hack.stock2u.authentication.service.client;

import java.util.Date;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class KakaoTokenResponse {
  private String token_type;
  private String access_token;
  private Date expires_in;
  private String refresh_token;
  private Date refresh_token_expires_in;
}
