package com.hack.stock2u.authentication.constant;

import static org.assertj.core.api.Assertions.*;

import com.hack.stock2u.constant.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SimpleUserRoleTest {

  @Test
  @DisplayName("UserRole.GENERAL 의 name 을 성공적으로 가져온다.")
  void getName() {
    String generalName = UserRole.GENERAL.getName();
    assertThat(generalName).isEqualTo("구매자");
  }

}