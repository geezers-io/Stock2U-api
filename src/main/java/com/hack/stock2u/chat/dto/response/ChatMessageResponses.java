package com.hack.stock2u.chat.dto.response;

import java.util.List;

public record ChatMessageResponses(
    List<ChatMessageResponse> chats
) {
}
