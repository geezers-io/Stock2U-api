package com.hack.stock2u.chat.dto.response;

public record ChatRoomResponse(
    Long id,
    String chatId,
    Long product,
    Long seller,
    Long customer
){


}
