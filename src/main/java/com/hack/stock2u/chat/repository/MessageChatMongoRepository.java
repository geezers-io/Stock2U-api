package com.hack.stock2u.chat.repository;

import com.hack.stock2u.models.ChatMessage;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageChatMongoRepository extends MongoRepository<ChatMessage, String> {
  
}
