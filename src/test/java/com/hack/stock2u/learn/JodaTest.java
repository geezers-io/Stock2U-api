package com.hack.stock2u.learn;

import static org.assertj.core.api.Assertions.*;

import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JodaTest {

  @Test
  @DisplayName("같은 날짜의 Joda Time 은 true 를 반환한다.")
  void returnTrueSameJodaTime() {
    // given
    long now = System.currentTimeMillis();
    Date start = new Date(now);
    Date end = new Date(now);
    LocalDateTime startLocalTime = new LocalDateTime(start);
    LocalDateTime endDateTime = new LocalDateTime(start);

    // when
    boolean equals = startLocalTime.equals(endDateTime);

    // then
    assertThat(equals).isTrue();
  }

  @Test
  @DisplayName("joda time 이 성공적으로 조작된다")
  void manipulateJodaTime() {
    // given
    LocalDateTime localDateTime = new LocalDateTime();

    // when
    DateTime time = new DateTime()
        .withHourOfDay(23)
        .withMinuteOfHour(59)
        .withSecondOfMinute(59);
    localDateTime = new LocalDateTime(time);

    // then
    assertThat(time.getHourOfDay()).isEqualTo(23);
    assertThat(time.getMinuteOfHour()).isEqualTo(59);
    assertThat(time.getSecondOfMinute()).isEqualTo(59);


  }

}
