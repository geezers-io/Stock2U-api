package com.hack.stock2u.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class OneTimeCodeGeneratorTest {
  Logger logger = LoggerFactory.getLogger("OneTimeCodeGeneratorTest");

  @Test
  @DisplayName("6자리 숫자 코드를 정상적으로 생성한다.")
  void create6DigitCode() {
    String code = OneTimeCodeGenerator.createDigit(6);
    logger.info("code: {}", code);
    char[] arr = code.toCharArray();

    for (var c : arr) {
      int num = Character.getNumericValue(c);
      assertThat(Integer.class.isInstance(num)).isTrue();
    }
  }

}
