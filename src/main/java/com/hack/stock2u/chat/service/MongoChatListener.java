package com.hack.stock2u.chat.service;

import com.hack.stock2u.global.service.SequenceGeneratorService;
import com.hack.stock2u.models.ChatMessage;
import com.hack.stock2u.models.embed.MongoAutoIncrementSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MongoChatListener extends AbstractMongoEventListener<MongoAutoIncrementSequence> {
  private final SequenceGeneratorService generatorService;

  @Override
  public void onBeforeConvert(BeforeConvertEvent<MongoAutoIncrementSequence> event) {
    long count = generatorService.generateSequence(ChatMessage.SEQUENCE_NAME);
    event.getSource().setSeq(count);
  }

}
