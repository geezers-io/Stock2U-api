package com.hack.stock2u.global.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class BasicLogFilter extends Filter<ILoggingEvent> {
  @Override
  public FilterReply decide(ILoggingEvent event) {
    String loggerName = event.getLoggerName();
    if (loggerName.equals(HttpLogger.class.getName())) {
      return FilterReply.DENY;
    }
    return FilterReply.ACCEPT;
  }
}
