package com.hack.mapstockapi.logback;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class SimpleLogTest {
	Logger logger = LoggerFactory.getLogger("test logger");

	@Test
	@DisplayName("로그가 수준별로 출력된다.")
	public void printSimpleLogger() {
		logger.info("인포 로그입니다.");
		logger.warn("워닝 로그입니다.");
		logger.error("에러 로그입니다.");
		logger.trace("추적 로그입니다.");
		logger.debug("디버그 로그입니다.");
	}

}
