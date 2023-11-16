package com.hack.stock2u.chat.repository;

import com.hack.stock2u.models.ChatMessage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageChatMongoRepository extends MongoRepository<ChatMessage, String> {

  long countByRoomIdAndReadIsFalseAndUsernameNot(Long roomId, String userName);

  Optional<ChatMessage> findTopByRoomIdOrderByCreatedAtDesc(Long id);

  @Query(value = "{ _id: { $lt:  ?1 }, room_id: { $eq: ?0 } }")
  List<ChatMessage> findChatHistory(Long roomId, Long cursor, Pageable pageable);

}
