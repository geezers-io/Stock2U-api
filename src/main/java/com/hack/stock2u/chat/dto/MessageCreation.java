package com.hack.stock2u.chat.dto;

import com.hack.stock2u.models.User;
import java.util.List;
import lombok.Builder;

@Builder
public record MessageCreation(
    User user,
    String profileImageUrl,
    String message,
    List<Long> imageIds
) {}
