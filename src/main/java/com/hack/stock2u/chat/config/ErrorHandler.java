package com.hack.stock2u.chat.config;

import com.hack.stock2u.global.exception.BasicException;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

public class ErrorHandler extends StompSubProtocolErrorHandler {

  @Override
  public Message<byte[]> handleClientMessageProcessingError(
      Message<byte[]> clientMessage, Throwable ex
  ) {
    if (ex instanceof BasicException) {
      HttpStatus httpStatus = ((BasicException) ex).getErrorResponse().httpStatus();
      if (httpStatus.value() == 401) {
        return createMessage(((BasicException) ex).getErrorResponse().message());
      }
    }
    return super.handleClientMessageProcessingError(clientMessage, ex);
  }

  private Message<byte[]> createMessage(String message) {
    StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
    accessor.setLeaveMutable(true);

    return MessageBuilder.createMessage(
        message.getBytes(StandardCharsets.UTF_8),
        accessor.getMessageHeaders()
    );
  }

}
