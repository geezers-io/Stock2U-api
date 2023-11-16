package com.hack.stock2u.chat.dto.response;

import com.hack.stock2u.models.ChatMessage;
import java.util.Date;
import java.util.List;
import lombok.Builder;

@Builder
public record ChatHistories(List<ChatMessage> histories, Long cursor, boolean last) {}
