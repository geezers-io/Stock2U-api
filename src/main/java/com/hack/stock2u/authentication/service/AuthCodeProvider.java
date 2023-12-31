package com.hack.stock2u.authentication.service;

import com.hack.stock2u.authentication.AuthException;
import com.hack.stock2u.authentication.service.client.AuthUserDetail;
import com.hack.stock2u.global.factory.SmsFactory;
import com.hack.stock2u.utils.Convertor;
import com.hack.stock2u.utils.OneTimeCodeGenerator;
import java.text.MessageFormat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthCodeProvider {
  private final RedisTemplate<String, String> template;
  private final String keyPrefix = "auth-verification-code";

  public void save(String phone, int minutes) {
    ValueOperations<String, String> ops = template.opsForValue();
    String newCode = OneTimeCodeGenerator.createDigit(6);

    String key = createKey(phone);
    ops.set(key, newCode);

    String message = MessageFormat.format(SmsMessage.SEND_CODE.getMessage(), newCode);
    String korPhone = new Convertor().phone(phone);
    String error =
        SmsFactory.getSimpleMessageCreator(korPhone, message).create().getErrorMessage();

    if (error != null) {
      log.error("twilio error: {}", error);
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    template.expire(key, minutes, TimeUnit.MINUTES);
  }

  public void saveAvailableSignup(String oauthId) {
    ValueOperations<String, String> ops = template.opsForValue();
    ops.set(oauthId, "ready");
    template.expire(oauthId, 1, TimeUnit.HOURS);
  }

  public boolean isReadySignup(String oauthId) {
    ValueOperations<String, String> ops = template.opsForValue();
    String ready = ops.get(oauthId);
    if (ready != null) {
      template.delete(oauthId);
    }
    return ready != null;
  }

  public void verifyCode(String phone, String code) {
    ValueOperations<String, String> ops = template.opsForValue();
    String key = createKey(phone);
    String target = ops.get(key);

    if (target == null) {
      throw AuthException.EXPIRED_AUTH_CODE.create();
    }

    if (target.equals("complete")) {
      throw AuthException.ALREADY_PASS_AUTH_CODE.create();
    }

    if (!target.equals(code)) {
      throw AuthException.MISMATCH_AUTH_CODE.create();
    }

    ops.set(key, "complete");
    template.expire(key, 10, TimeUnit.MINUTES);
  }

  public boolean checkVerifyComplete(String phone) {
    ValueOperations<String, String> ops = template.opsForValue();
    String key = createKey(phone);
    String value = ops.get(key);
    if (value == null) {
      return false;
    }
    return value.equals("complete");
  }

  private String createKey(String phone) {
    return keyPrefix + ":" + phone;
  }

}
