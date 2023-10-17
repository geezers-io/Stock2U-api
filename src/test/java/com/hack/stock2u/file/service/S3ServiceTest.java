package com.hack.stock2u.file.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class S3ServiceTest {
  S3Service s3Service = new S3Service(null);

  @Test
  @DisplayName("s3 이미지 경로에서 key 값을 성공적으로 가져온다.")
  void getKey() {
    // given
    String url = "https://stock2u-bucket.s3.ap-northeast-2.amazonaws.com/programmers-icon.png";

    // when
    String key = s3Service.getKey(url);

    // then
    assertThat(key).isEqualTo("programmers-icon.png");
  }
}