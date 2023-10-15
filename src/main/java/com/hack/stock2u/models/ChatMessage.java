package com.hack.stock2u.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Filter;
import org.springframework.data.annotation.Id;
import java.util.Date;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "chat_messages")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

  @Id
  @Field(value = "_id", targetType = FieldType.OBJECT_ID)
  private String id;

  @Field(name = "room_id")
  private Long roomId;

  @Field(name = "message")
  private String message;

  @Field(name = "timestamp")
  private Date timestamp;


}