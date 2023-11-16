package com.hack.stock2u.chat.service;

import com.hack.stock2u.chat.dto.response.ChatHistories;
import com.hack.stock2u.chat.repository.MessageChatMongoRepository;
import com.hack.stock2u.models.ChatMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatHistoryLoader {
  private final MessageChatMongoRepository chatMongoRepository;
  private final PageRequest pageable = PageRequest
      .of(0, 30)
      .withSort(Sort.Direction.DESC, "timestamp");

  public ChatHistories getHistories(Long reservationId, Long cursor) {
    Optional<ChatMessage> firstMessage =
        chatMongoRepository.findTopByRoomIdOrderByCreatedAtDesc(reservationId);

    if (firstMessage.isEmpty()) {
      return new ChatHistories(new ArrayList<>(), null, true);
    }

    cursor = Objects.requireNonNullElse(cursor, firstMessage.get().getId() + 1);

    List<ChatMessage> histories = chatMongoRepository.findChatHistory(
        reservationId,
        cursor,
        pageable
    );

    Collections.reverse(histories);

    ChatHistories.ChatHistoriesBuilder historiesBuilder =
        ChatHistories.builder().histories(histories);

    // 조회된 내역이 있다면
    if (histories.size() > 0) {
      ChatMessage chatMessage = histories.get(0);
      return historiesBuilder
          .cursor(chatMessage.getId())
          .last(false)
          .build();
    }

    // 조회된 내역이 없다면
    return historiesBuilder
        .cursor(null)
        .last(true)
        .build();
  }

}
