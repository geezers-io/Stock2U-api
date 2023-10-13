package com.hack.stock2u.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ConvertorTest {
  final Convertor convertor = new Convertor();
  Logger logger = LoggerFactory.getLogger("OneTimeCodeGeneratorTest");

  @Test
  @DisplayName("휴대폰 번호를 성공적으로 한국번호로 변환한다.")
  void convertKorPhone() {
    // given
    String p = "01026554276";

    // when
    String phone = convertor.phone(p);

    // then
    logger.warn("phone: {}", phone);
    assertThat(phone).startsWith("+82");
  }

}
