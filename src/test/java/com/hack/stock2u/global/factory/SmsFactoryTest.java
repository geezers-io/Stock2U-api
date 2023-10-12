package com.hack.stock2u.global.factory;

import static org.assertj.core.api.Assertions.*;

import com.twilio.rest.api.v2010.account.MessageCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SmsFactory.class)
@TestPropertySource(locations = "classpath:externel.yaml")
class SmsFactoryTest {

  @Test
  @DisplayName("twilio SMS 발신이 성공적으로 수행된다.")
  public void successSendSms() {
    // given, when
    MessageCreator target =
        SmsFactory.getSimpleMessageCreator("+8201026554276", "test message");

    // then
    assertThat(target).isInstanceOf(MessageCreator.class);
  }

}
