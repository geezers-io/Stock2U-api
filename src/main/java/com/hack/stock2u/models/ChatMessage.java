package com.hack.stock2u.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hack.stock2u.constant.ChatMessageType;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "chat_messages")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

  @Transient
  public static final String SEQUENCE_NAME = "chat_message_sequence";

  @Id
  @Field(value = "_id", targetType = FieldType.INT64)
  @JsonIgnore
  private Long id;

  @Field(name = "room_id")
  @JsonIgnore
  private Long roomId;

  @Field(name = "user_name")
  private String username;

  @Field(name = "type")
  private ChatMessageType type;

  @Field(name = "message")
  private String message;

  @Field(name = "image_ids")
  private List<Long> imageIds;

  @Field(name = "timestamp")
  private Date createdAt;

  @Field(name = "message_read")
  private boolean read;

}
