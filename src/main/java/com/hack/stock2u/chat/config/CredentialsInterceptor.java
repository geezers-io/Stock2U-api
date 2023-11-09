package com.hack.stock2u.chat.config;

import com.hack.stock2u.authentication.AuthException;
import com.hack.stock2u.authentication.dto.SessionUser;
import com.hack.stock2u.authentication.service.SessionManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CredentialsInterceptor implements ChannelInterceptor {
  private final SessionManager sessionManager;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    log.info(message.getHeaders().toString());
    List<String> userIds = accessor.getNativeHeader("userId");
    List<String> phones = accessor.getNativeHeader("phone");

    if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
      return ChannelInterceptor.super.preSend(message, channel);
    }

    if (StompCommand.UNSUBSCRIBE.equals(accessor.getCommand())) {
      return ChannelInterceptor.super.preSend(message, channel);
    }

    log.info("userIds: {}, phones: {}", userIds, phones);
    if (userIds == null || phones == null) {
      throw AuthException.IS_ANONYMOUS_USER.create();
    }

    String userId = userIds.get(0);
    String phone = phones.get(0);

    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
      String key = sessionManager.getKey(phone, userId);
      SessionUser user = sessionManager.getSessionUser(key);
      if (user == null) {
        throw AuthException.IS_ANONYMOUS_USER.create();
      }
    }

    return ChannelInterceptor.super.preSend(message, channel);
  }

}
