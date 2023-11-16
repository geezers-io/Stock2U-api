package com.hack.stock2u.global.service;

import static org.springframework.data.mongodb.core.query.Criteria.*;

import com.hack.stock2u.models.embed.MongoAutoIncrementSequence;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SequenceGeneratorService {
  private final MongoOperations operations;

  public long generateSequence(String seqName) {
    MongoAutoIncrementSequence counter = operations.findAndModify(
        Query.query(where("_id").is(seqName)),
        new Update().inc("seq", 1),
        FindAndModifyOptions.options().returnNew(true).upsert(true),
        MongoAutoIncrementSequence.class
    );

    return Objects.nonNull(counter) ? counter.getSeq() + 1 : 1;
  }

}
