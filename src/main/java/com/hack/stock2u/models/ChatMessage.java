package com.hack.stock2u.models;

import java.util.Date;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
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

  @Field(name = "user_name")
  private String userName;

  @Field(name = "message")
  private String message;

  @Field(name = "timestamp")
  private Date createdAt;


}