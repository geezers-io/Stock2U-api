package com.hack.stock2u.global.factory;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsFactory {
  private static String sid;
  private static String token;
  private static String phone;

  @Autowired
  public SmsFactory(
      @Value("${app.twilio.sid}") String sid,
      @Value("${app.twilio.token}") String token,
      @Value("${app.twilio.phone}") String phone
  ) {
    log.warn("sid: {}, token: {}, phone: {}", sid, token, phone);
    SmsFactory.sid = sid;
    SmsFactory.token = token;
    SmsFactory.phone = phone;
  }

  public static MessageCreator getSimpleMessageCreator(String targetPhone, String message) {
    Twilio.init(sid, token);
    return Message.creator(
        new PhoneNumber(targetPhone),
        new PhoneNumber(phone),
        message
    );
  }

  @Value("${app.twilio.sid}")
  public static void setSid(String sid) {
    SmsFactory.sid = sid;
  }

  @Value("${app.twilio.token}")
  public static void setToken(String token) {
    SmsFactory.token = token;
  }

  @Value("${app.twilio.phone}")
  public static void setPhone(String phone) {
    SmsFactory.phone = phone;
  }

}
